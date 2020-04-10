package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.ReportResult12Contract
import com.sm.nadaman.mvp.model.ReportResult12Model

class ReportResult12Presenter(iReportResult12View: ReportResult12Contract.IReportResult12View) :
    BasePresenter<ReportResult12Contract.IReportResult12Model, ReportResult12Contract.IReportResult12View>(
        iReportResult12View
    ), ReportResult12Contract.IReportResult12Presenter {

    override val model: ReportResult12Contract.IReportResult12Model
        get() = ReportResult12Model()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
