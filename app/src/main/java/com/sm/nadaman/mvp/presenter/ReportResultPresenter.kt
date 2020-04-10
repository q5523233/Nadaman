package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.ReportResultContract
import com.sm.nadaman.mvp.model.ReportResultModel

class ReportResultPresenter(iReportResultView: ReportResultContract.IReportResultView) :
    BasePresenter<ReportResultContract.IReportResultModel, ReportResultContract.IReportResultView>(
        iReportResultView
    ), ReportResultContract.IReportResultPresenter {

    override val model: ReportResultContract.IReportResultModel
        get() = ReportResultModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
