package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.ReportListContract
import com.sm.nadaman.mvp.model.ReportListModel

class ReportListPresenter(iReportListView: ReportListContract.IReportListView) :
    BasePresenter<ReportListContract.IReportListModel, ReportListContract.IReportListView>(
        iReportListView
    ), ReportListContract.IReportListPresenter {

    override val model: ReportListContract.IReportListModel
        get() = ReportListModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
