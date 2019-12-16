package com.sm.nadaman.ui.mvp.model

import com.blackflagbin.kcommon.base.BaseModel
import com.sm.nadaman.ui.mvp.contract.LoginContract
import com.sm.nadaman.ui.common.http.ApiService
import com.sm.nadaman.ui.common.http.CacheService

class LoginModel : BaseModel<ApiService, CacheService>(), LoginContract.ILoginModel {

}
