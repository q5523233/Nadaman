package com.sm.nadaman.common.net

import android.os.Parcelable
import com.blackflagbin.kcommon.entity.net.IHttpResultEntity
import com.blankj.utilcode.util.SPUtils
import com.techne.nomnompos.common.SP_NAME
import com.techne.nomnompos.common.getTimeFormat
import kotlinx.android.parcel.Parcelize
import java.util.*


//最外层数据类
data class HttpResultEntity<T>(
    var code: Int = 0,
    var message: String = "",
    var data: T
) : IHttpResultEntity<T> {
    override val isSuccess: Boolean
        get() = code == 0
    override val errorCode: Int
        get() = code
    override val errorMessage: String
        get() = message
    override val result: T
        get() = data
}

data class UserEntity(val name:String){

    var isSignIn = false
        get() = !SPUtils.getInstance(SP_NAME).getString("token", "").isNullOrEmpty()
}