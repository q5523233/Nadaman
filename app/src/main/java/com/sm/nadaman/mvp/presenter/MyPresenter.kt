package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.MyContract
import com.sm.nadaman.mvp.model.MyModel

class MyPresenter(iMyView: MyContract.IMyView) :
    BasePresenter<MyContract.IMyModel, MyContract.IMyView>(iMyView), MyContract.IMyPresenter {

    override val model: MyContract.IMyModel
        get() = MyModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
