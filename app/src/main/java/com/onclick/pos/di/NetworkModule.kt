package com.onclick.task.di

import android.content.Context
import com.onclick.task.BuildConfig.*
import com.onclick.task.data.api.ApiService
import com.onclick.task.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    /**
     * provide Http Interceptor to be used in logging networking
     *
     * @return an instance of Http Interceptor with custom logging
     */


    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply { level = BODY }


    /**
     * Provide Http Interceptor to add "Authorization" and "Accept-Language" to every requests
     *
     * @param context Context the application context to be used to get current lang
     * @param userAuthDao UserAuthDao the dao of user to get user from db
     * @return Interceptor instance with headers
     */
    @Provides
    @Singleton
    @HeadersInterceptor
    fun provideHttpHeadersInterceptor(
        context: Context,

        ): Interceptor {

        return Interceptor { chain ->
            var request = chain.request()


            request = request.newBuilder()
                .header(
                    "Authorization",
                    "Bearer 368e0734f1b97d289c41dab0fc9067c7c7fb42ff4a9a33b97221177d6df87953"
                ).build()


            chain.proceed(request)
        }
    }


    /**
     * handle 401 error if user is already logged in delete user from db and return error code as 409
     * if user is not logged in this mean it's a login request so do nothing.
     *
     * @param userAuthDao UserAuthDao UserAuthDao the dao of user to mange user from db
     * @return Interceptor the interceptor with error handled
     */
    @Provides
    @Singleton
    @ErrorInterceptor
    fun provideErrorInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val response = chain.proceed(request)


//            //if origin request don't have Authorization header
            if (response.code != 401)
                return@Interceptor response

            synchronized(this) {

                // Access token is refreshed in another thread.

                return@Interceptor chain.proceed(
                    response.request.newBuilder()
                        .header(
                            "Authorization",
                            "Bearer 368e0734f1b97d289c41dab0fc9067c7c7fb42ff4a9a33b97221177d6df87953"
                        )
                        .build()
                )


            }


        }
    }


    /**
     * Provides the okHttp client for networking
     *
     * @param loggingInterceptor the okHttp logging interceptor
     * @param headersInterceptor the interceptor to add headers to all requests
     * @return okHttp client instance
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @HeadersInterceptor headersInterceptor: Interceptor,
//        @ErrorInterceptor errorInterceptor: Interceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()

        client
            .addInterceptor(headersInterceptor)
//            .addInterceptor(errorInterceptor)
            .connectTimeout(300, SECONDS) // connect timeout
            .readTimeout(300, SECONDS)    // socket timeout
            .writeTimeout(300, SECONDS)    // request timeout

        if (DEBUG)
            client.addInterceptor(loggingInterceptor)

        return client.build()
    }


    /**
     * Provides the Retrofit instance with [BASE_URL]
     *
     * @param httpClient the okHttp client
     * @return the Retrofit instance
     */
    @Provides
    @Singleton
    @NormalRequest
    fun provideRetrofitInterface(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
            .baseUrl("https://api.youneedabudget.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(httpClient)
            .build()
    }

    /**
     * Provides the service implementation with [NormalRequest]
     *
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the ApiService implementation.
     */
    @Provides
    @Singleton
    @NormalRequest
    fun provideApiService(@NormalRequest retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    /**
     * Provides the Retrofit instance with [BASE_IDENTITY_URL]
     *
     * @param httpClient the okHttp client
     * @return the Retrofit instance
     */
    @Provides
    @Singleton
    @IdentityRequest
    fun provideRetrofitInterfaceForIdentity(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
//            .baseUrl(BASE_IDENTITY_URL)
            .baseUrl("https://api.youneedabudget.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(httpClient)
            .build()
    }

    /**
     * Provides the service implementation with [IdentityRequest]
     *
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the ApiService implementation.
     */
    @Provides
    @Singleton
    @IdentityRequest
    fun provideApiServiceForIdentity(@IdentityRequest retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}



