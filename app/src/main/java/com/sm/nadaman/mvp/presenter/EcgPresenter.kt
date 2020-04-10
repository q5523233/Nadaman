package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.EcgContract
import com.sm.nadaman.mvp.model.EcgModel

class EcgPresenter(iEcgView: EcgContract.IEcgView) :
    BasePresenter<EcgContract.IEcgModel, EcgContract.IEcgView>(iEcgView),
    EcgContract.IEcgPresenter {

    override val model: EcgContract.IEcgModel
        get() = EcgModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
