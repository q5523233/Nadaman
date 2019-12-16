package com.sm.nadaman.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sm.nadaman.R
import com.sm.nadaman.ui.adapter.WellComeAdapter
import com.trello.rxlifecycle2.components.RxActivity
import kotlinx.android.synthetic.main.activity_wellcome.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.startActivity


class WellComeActivity : RxActivity() {

    val list: List<View> by lazy {
        ArrayList<View>().apply {
            add(getPageItem(0))
            add(getPageItem(1))
            add(getPageItem(2))
            add(getPageItem(3))
        }
    }

    private fun getPageItem(i: Int): View {
        return LayoutInflater.from(this).inflate(
            R.layout.page_item, null
        ).apply {
            findViewById<ImageView>(R.id.background).setImageResource(
                when (i) {
                    0 -> R.mipmap.splsh1
                    1 -> R.mipmap.splsh2
                    2 -> R.mipmap.splsh3
                    else -> R.mipmap.splsh4
                }
            )
            findViewById<ImageView>(R.id.text).setImageResource(
                when (i) {
                    0 -> R.mipmap.splshtext1
                    1 -> R.mipmap.splshtext2
                    2 -> R.mipmap.splshtext3
                    else -> R.mipmap.splshtext4
                }
            )
        }
    }


    val pagerAdapter by lazy {
        WellComeAdapter(list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellcome)
        viewpage.adapter = pagerAdapter
        viewpage.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> iv_indicator.setImageResource(R.mipmap.indicator1)
                    1 -> iv_indicator.setImageResource(R.mipmap.indicator2)
                    2 -> iv_indicator.setImageResource(R.mipmap.indicator3)
                    else -> iv_indicator.setImageResource(R.mipmap.indicato4)
                }
            }
        })

        iv_indicator.setOnClickListener {
            if (viewpage.currentItem == 3){
                startActivity(Intent(this,SelectEcgActivity::class.java).clearTop())
            }
        }
    }
}