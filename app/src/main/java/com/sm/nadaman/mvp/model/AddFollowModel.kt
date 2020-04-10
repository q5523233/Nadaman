package com.sm.nadaman.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.mvp.contract.AddFollowContract
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService

class AddFollowModel : BaseModel<ApiService, CacheService>(), AddFollowContract.IAddFollowModel {

}
