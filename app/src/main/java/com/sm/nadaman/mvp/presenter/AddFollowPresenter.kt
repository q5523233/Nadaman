package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.AddFollowContract
import com.sm.nadaman.mvp.model.AddFollowModel

class AddFollowPresenter(iAddFollowView: AddFollowContract.IAddFollowView) :
    BasePresenter<AddFollowContract.IAddFollowModel, AddFollowContract.IAddFollowView>(
        iAddFollowView
    ), AddFollowContract.IAddFollowPresenter {

    override val model: AddFollowContract.IAddFollowModel
        get() = AddFollowModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
