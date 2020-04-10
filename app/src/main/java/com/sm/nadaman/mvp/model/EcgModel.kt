package com.sm.nadaman.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.mvp.contract.EcgContract
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService

class EcgModel : BaseModel<ApiService, CacheService>(), EcgContract.IEcgModel {

}
