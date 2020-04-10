package com.sm.nadaman.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.sm.nadaman.R
import com.zhouwei.mzbanner.holder.MZViewHolder

class BannerViewHolder : MZViewHolder<Int> {
    lateinit var imageView: ImageView
    override fun onBind(p0: Context?, p1: Int, p2: Int?) {
        p2?.let { imageView.setImageResource(it) }
    }

    override fun createView(p0: Context?): View {

        val view = LayoutInflater.from(p0).inflate(R.layout.banner_item, null)
        imageView = view.findViewById(R.id.image)
        return view
    }
}