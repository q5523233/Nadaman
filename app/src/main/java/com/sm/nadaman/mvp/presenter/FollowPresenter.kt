package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.FollowContract
import com.sm.nadaman.mvp.model.FollowModel

class FollowPresenter(iFollowView: FollowContract.IFollowView) :
    BasePresenter<FollowContract.IFollowModel, FollowContract.IFollowView>(iFollowView),
    FollowContract.IFollowPresenter {

    override val model: FollowContract.IFollowModel
        get() = FollowModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
