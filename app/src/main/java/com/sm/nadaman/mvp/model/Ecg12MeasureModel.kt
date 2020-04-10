package com.sm.nadaman.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.mvp.contract.Ecg12MeasureContract
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService

class Ecg12MeasureModel : BaseModel<ApiService, CacheService>(),
    Ecg12MeasureContract.IEcg12MeasureModel {

}
