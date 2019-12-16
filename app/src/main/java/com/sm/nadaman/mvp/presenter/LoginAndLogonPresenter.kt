package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.LoginAndLogonContract
import com.sm.nadaman.mvp.model.LoginAndLogonModel

class LoginAndLogonPresenter(iLoginAndLogonView: LoginAndLogonContract.ILoginAndLogonView) :
    BasePresenter<LoginAndLogonContract.ILoginAndLogonModel, LoginAndLogonContract.ILoginAndLogonView>(
        iLoginAndLogonView
    ), LoginAndLogonContract.ILoginAndLogonPresenter {

    override val model: LoginAndLogonContract.ILoginAndLogonModel
        get() = LoginAndLogonModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
