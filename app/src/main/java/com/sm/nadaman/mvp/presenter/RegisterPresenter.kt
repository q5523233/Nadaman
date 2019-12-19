package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.RegisterContract
import com.sm.nadaman.mvp.model.RegisterModel

class RegisterPresenter(iRegisterView: RegisterContract.IRegisterView) :
    BasePresenter<RegisterContract.IRegisterModel, RegisterContract.IRegisterView>(iRegisterView),
    RegisterContract.IRegisterPresenter {

    override val model: RegisterContract.IRegisterModel
        get() = RegisterModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
