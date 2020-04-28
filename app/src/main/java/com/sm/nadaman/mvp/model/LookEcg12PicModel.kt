package com.sm.nadaman.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.mvp.contract.LookEcg12PicContract
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService

class LookEcg12PicModel : BaseModel<ApiService, CacheService>(),
    LookEcg12PicContract.ILookEcg12PicModel {

}
