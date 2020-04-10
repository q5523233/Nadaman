package com.sm.nadaman.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sm.nadaman.R
import com.sm.nadaman.common.bean.Health
import java.util.*

class EcgDataAdapter(data: ArrayList<Health>, layoutResIdRes: Int = R.layout.item_ecg_data) :
    BaseQuickAdapter<Health, BaseViewHolder>(layoutResIdRes, data) {
    override fun convert(helper: BaseViewHolder?, item: Health?) {
        item?.let {
            helper?.apply {
                getView<TextView>(R.id.tv_ave).text = "平均心率(bpm):${it.aveHeartRate}"
                getView<TextView>(R.id.tv_max).text = "最高心率(bpm):${it.maxHeartRate}"
                getView<TextView>(R.id.tv_min).text = "最低心率(bpm):${it.minHeartRate}"
                getView<TextView>(R.id.tv_date).text = it.date
                getView<TextView>(R.id.tv_time).text =
//                    "测量时长:${Date(it.measureDuration.toLong()).getFormatTime("mm分ss秒")}"
                    "测量时长:${it.measureDuration}秒"
                // 0 正常 2轻度 4中度 6重度
                getView<ImageView>(R.id.iv).setImageResource(
                    when (it.exceptionLevel) {
                        2 -> R.mipmap.text_light
                        4 -> R.mipmap.text_medium
                        6 -> R.mipmap.text_strong
                        else -> R.mipmap.text_normal
                    }
                )
            }
        }

    }
}