package com.sm.nadaman.ui.adapter

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup



class WellComeAdapter(val list: List<View>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return list.size
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(list[position])
        return list[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(list[position])
    }
}