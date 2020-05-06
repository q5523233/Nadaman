package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.DisclaimerContract
import com.sm.nadaman.mvp.model.DisclaimerModel

class DisclaimerPresenter(iDisclaimerView: DisclaimerContract.IDisclaimerView) :
    BasePresenter<DisclaimerContract.IDisclaimerModel, DisclaimerContract.IDisclaimerView>(
        iDisclaimerView
    ), DisclaimerContract.IDisclaimerPresenter {

    override val model: DisclaimerContract.IDisclaimerModel
        get() = DisclaimerModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
