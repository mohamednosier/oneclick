package com.onclick.task.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.onclick.task.R
import com.onclick.task.databinding.ActivityMainBinding
import com.onclick.task.databinding.NavHeaderBinding
import com.onclick.task.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private lateinit var drawer: DrawerLayout

    private val viewModel by viewModels<MainViewModel>()

    override fun getLayoutRes() = R.layout.activity_main

    override fun init() {
//        val toolbar = binding.toolbar
//        setSupportActionBar(toolbar)
//        drawer = binding.drawerLayout
//        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
//            this, drawer, toolbar,
//            R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        drawer.addDrawerListener(toggle)
//        toggle.syncState()
        setupSideMenu()
    }

    @Override
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    /**
     * init the side menu ui and logic and handle working with all fragments
     */
    private fun setupSideMenu() {

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration(binding.mainNavigationView.menu, binding.drawerLayout)

        binding.mainNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevelDestination =
                appBarConfiguration.topLevelDestinations.contains(destination.id)
            val lockMode = if (isTopLevelDestination) {
                DrawerLayout.LOCK_MODE_UNLOCKED
            } else {
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            }
            binding.drawerLayout.setDrawerLockMode(lockMode)
        }

        initHeader()
//        initFooter()
//        initChangeLang()
//        initLogout()
    }

    /**
     * init side menu header to load user data and credit
     */
    private fun initHeader() {
        val header = NavHeaderBinding.inflate(layoutInflater)
            .apply {
                lifecycleOwner = this@MainActivity
                root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelOffset(R.dimen.x270dp)
                )
            }

//        viewModel.userName.observe(this, header.tvUserName::setText)

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                //mohamed nosier
//                viewModel.getUserCredit().observe(this@MainActivity) {
//                    header.isLoading = it.status == Status.LOADING
//
//                    header.tvWallet.text =
//                        if (it.data != null) it.data.credit else getString(R.string.n_a)
//                }
//                viewModel.getUserCredit1().observe(this@MainActivity) {
//                    header.isLoading = it.status == Status.LOADING
//
//                    header.tvWallet.text =
//                        if (it.data != null) it.data.userProfileDetails.contactDetails?.firstName+it.data.userProfileDetails.contactDetails?.lastName else getString(R.string.n_a)
//                }
            }

            override fun onDrawerClosed(drawerView: View) {}

            override fun onDrawerStateChanged(newState: Int) {}

        })


        binding.mainNavigationView.addHeaderView(header.root)
    }
}