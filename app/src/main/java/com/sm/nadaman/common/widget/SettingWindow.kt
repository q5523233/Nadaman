package com.sm.nadaman.common.widget

import android.content.Context
import android.view.View.OnClickListener
import android.widget.RadioButton
import com.blankj.utilcode.util.SnackbarUtils.dismiss
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.core.DrawerPopupView
import com.sm.nadaman.R
import kotlinx.android.synthetic.main.setting_item.view.*

class SettingWindow(mContext: Context,val callback: (c:ConfigBean)->Unit) : BottomPopupView(mContext) {

    internal lateinit var configBean: ConfigBean

    internal var selectedIndex = ByteArray(5)
    override fun getImplLayoutId(): Int {
        return R.layout.setting_item
    }

    override fun onCreate() {
        super.onCreate()
        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_confirm.setOnClickListener(OnClickListener {
            dismiss()
            getCheckResult()
        })
        (rg_display.getChildAt(selectedIndex[0].toInt()) as RadioButton).isChecked = true
        (rg_mode.getChildAt(selectedIndex[1].toInt()) as RadioButton).isChecked = true
        (rg_measure_time.getChildAt(selectedIndex[2].toInt()) as RadioButton).isChecked = true
        (rg_speed.getChildAt(selectedIndex[3].toInt()) as RadioButton).isChecked = true
        (rg_gain.getChildAt(selectedIndex[4].toInt()) as RadioButton).isChecked = true

    }

    private fun getCheckResult() {
        for (i in 0 until rg_display.childCount) {
            val checked = (rg_display.getChildAt(i) as RadioButton).isChecked
            if (checked) {
                selectedIndex[0] = i.toByte()
                break
            }
        }

        for (i in 0 until rg_mode.childCount) {
            val checked = (rg_mode.getChildAt(i) as RadioButton).isChecked
            if (checked) {
                selectedIndex[1] = i.toByte()
                break
            }
        }

        for (i in 0 until rg_measure_time.childCount) {
            val checked = (rg_measure_time.getChildAt(i) as RadioButton).isChecked
            if (checked) {
                selectedIndex[2] = i.toByte()
                break
            }
        }
        for (i in 0 until rg_speed.childCount) {
            val checked = (rg_speed.getChildAt(i) as RadioButton).isChecked
            if (checked) {
                selectedIndex[3] = i.toByte()
                break
            }
        }
        for (i in 0 until rg_gain.childCount) {
            val checked = (rg_gain.getChildAt(i) as RadioButton).isChecked
            if (checked) {
                selectedIndex[4] = i.toByte()
                break
            }
        }
        configBean = ConfigBean()
        configBean.setConfig(selectedIndex)
        callback.invoke(configBean)
    }

    class ConfigBean {
        lateinit var selectedIndex: ByteArray

        var mode: Int = 0
        var gain: Float = 0.toFloat()
        var speed: Float = 0.toFloat()
        var displayMode: Int = 0
        var measure: Int = 0

        constructor()

        constructor(mode: Int, gain: Float, speed: Float, displayMode: Int, measure: Int) {
            this.mode = mode
            this.gain = gain
            this.speed = speed
            this.displayMode = displayMode
            this.measure = measure
        }

        fun setConfig(selectedIndex: ByteArray) {
            this.selectedIndex = selectedIndex
            this.measure = selectedIndex[2].toInt()
            mode = selectedIndex[1].toInt()
            gain = (Math.pow(2.0, selectedIndex[4].toDouble()) * 2.5f).toFloat()
            speed = (12.5f * Math.pow(2.0, selectedIndex[3].toDouble())).toFloat()
            displayMode = selectedIndex[0].toInt()
        }

    }
}