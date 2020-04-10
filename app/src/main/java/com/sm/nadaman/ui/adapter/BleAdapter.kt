package com.sm.nadaman.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.clj.fastble.data.BleDevice
import com.sm.nadaman.R

class BleAdapter(data: ArrayList<BleDevice>, layoutResIdRes: Int = R.layout.item_ble) :
    BaseQuickAdapter<BleDevice, BaseViewHolder>(layoutResIdRes, data) {
    override fun convert(helper: BaseViewHolder?, item: BleDevice?) {
        item?.let {
            helper?.apply {
                getView<TextView>(R.id.tv_ble_name).text = it.name
            }
        }

    }
}