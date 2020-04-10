package com.sm.nadaman.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.clj.fastble.data.BleDevice
import com.sm.nadaman.R
import com.sm.nadaman.common.bean.Friend
import com.sm.nadaman.common.loadImage

class FollowAdapter(data: ArrayList<Friend>, layoutResIdRes: Int = R.layout.item_follow) :
    BaseQuickAdapter<Friend, BaseViewHolder>(layoutResIdRes, data) {
    override fun convert(helper: BaseViewHolder?, item: Friend?) {
        item?.let {
            helper?.apply {
                getView<TextView>(R.id.tv_name).text = it.nickName
                getView<TextView>(R.id.tv_moblie).text = it.phone
                getView<TextView>(R.id.tv_type).text = it.flag
                getView<ImageView>(R.id.iv_head).loadImage(it.imgPath)


            }
        }

    }
}