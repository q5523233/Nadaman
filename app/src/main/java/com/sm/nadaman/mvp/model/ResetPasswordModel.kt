package com.sm.nadaman.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.mvp.contract.ResetPasswordContract
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService

class ResetPasswordModel : BaseModel<ApiService, CacheService>(), ResetPasswordContract.IResetPasswordModel {

}
