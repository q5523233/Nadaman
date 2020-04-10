package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.Ecg12MeasureContract
import com.sm.nadaman.mvp.model.Ecg12MeasureModel

class Ecg12MeasurePresenter(iEcg12MeasureView: Ecg12MeasureContract.IEcg12MeasureView) :
    BasePresenter<Ecg12MeasureContract.IEcg12MeasureModel, Ecg12MeasureContract.IEcg12MeasureView>(
        iEcg12MeasureView
    ), Ecg12MeasureContract.IEcg12MeasurePresenter {

    override val model: Ecg12MeasureContract.IEcg12MeasureModel
        get() = Ecg12MeasureModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
