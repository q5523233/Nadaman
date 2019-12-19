package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.LoginContract
import com.sm.nadaman.mvp.model.LoginModel

class LoginPresenter(iLoginView: LoginContract.ILoginView) :
    BasePresenter<LoginContract.ILoginModel, LoginContract.ILoginView>(iLoginView), LoginContract.ILoginPresenter {

    override val model: LoginContract.ILoginModel
        get() = LoginModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
