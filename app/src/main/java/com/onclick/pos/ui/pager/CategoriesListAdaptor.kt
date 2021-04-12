package com.onclick.task.ui.pager

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.onclick.task.R

class CategoriesListAdaptor(
    private val context: Activity,
    private val title: Array<String>,
    private val description: Array<String>,
    private val imgid: Array<Int>
) : ArrayAdapter<String>(context, R.layout.category_card, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.category_card, null, true)

        val titleText = rowView.findViewById(R.id.tv_catogory_name) as TextView
        val imageView = rowView.findViewById(R.id.IV_categoryCard) as ImageView
        val subtitleText = rowView.findViewById(R.id.tv_categoryCard_des) as TextView

        titleText.text = title[position]
        imageView.setImageResource(imgid[position])
        subtitleText.text = description[position]

        return rowView
    }
}