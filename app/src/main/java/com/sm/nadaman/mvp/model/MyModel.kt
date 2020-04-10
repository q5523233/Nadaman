package com.sm.nadaman.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.mvp.contract.MyContract
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService

class MyModel : BaseModel<ApiService, CacheService>(), MyContract.IMyModel {

}
