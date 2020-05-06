package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.HandleContract
import com.sm.nadaman.mvp.model.HandleModel

class HandlePresenter(iHandleView: HandleContract.IHandleView) :
    BasePresenter<HandleContract.IHandleModel, HandleContract.IHandleView>(iHandleView),
    HandleContract.IHandlePresenter {

    override val model: HandleContract.IHandleModel
        get() = HandleModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
