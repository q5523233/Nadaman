package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.EcgMeasureContract
import com.sm.nadaman.mvp.model.EcgMeasureModel

class EcgMeasurePresenter(iEcgMeasureView: EcgMeasureContract.IEcgMeasureView) :
    BasePresenter<EcgMeasureContract.IEcgMeasureModel, EcgMeasureContract.IEcgMeasureView>(
        iEcgMeasureView
    ), EcgMeasureContract.IEcgMeasurePresenter {

    override val model: EcgMeasureContract.IEcgMeasureModel
        get() = EcgMeasureModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
