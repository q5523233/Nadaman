package com.sm.nadaman.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.LoginContract

class LoginModel : BaseModel<ApiService, CacheService>(), LoginContract.ILoginModel {

}
