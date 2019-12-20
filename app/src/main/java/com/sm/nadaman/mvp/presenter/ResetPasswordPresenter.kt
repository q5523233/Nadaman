package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.ResetPasswordContract
import com.sm.nadaman.mvp.model.ResetPasswordModel

class ResetPasswordPresenter(iResetPasswordView: ResetPasswordContract.IResetPasswordView) :
    BasePresenter<ResetPasswordContract.IResetPasswordModel, ResetPasswordContract.IResetPasswordView>(
        iResetPasswordView
    ), ResetPasswordContract.IResetPasswordPresenter {

    override val model: ResetPasswordContract.IResetPasswordModel
        get() = ResetPasswordModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
