package com.sm.nadaman.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.mvp.contract.ReportResult12Contract
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService

class ReportResult12Model : BaseModel<ApiService, CacheService>(),
    ReportResult12Contract.IReportResult12Model {

}
