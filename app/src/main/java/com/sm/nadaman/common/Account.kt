package com.sm.nadaman.common

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.blackflagbin.kcommon.base.BaseFragment
import com.blackflagbin.kcommon.facade.CommonLibrary
import com.techne.nomnompos.app.App
import com.sm.nadaman.common.net.HttpResultEntity
import com.sm.nadaman.common.net.UserEntity
import com.techne.nomnompos.common.SP_NAME
import com.techne.nomnompos.common.gson
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask


object Account {


    const val KEY_LOCAL_USER = "local_user_model"
    //未注册
    const val UNREGISTERED = 70001

    const val ERRORPASSWORD = 70010

    private lateinit var preferences: SharedPreferences

    operator fun invoke(app: Application) {
        preferences =
                app.applicationContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

        userModel.value = _userModel
    }

    var isLogin = false
        private set
        get() = _userModel?.isSignIn ?: false

    var token: String? = null
        set(value) {
            field = value
            CommonLibrary.instance.headerMap = hashMapOf( "token" to (value?:""))
            add("token", value?:"")
        }
        get() = getS("token","")


    fun saveUser( pf: UserEntity?){
        _userModel = pf
        saveUserEntity()
    }

    fun checkSignIn(context: BaseFragment<*, *, *, *>, code:Int = 0): Boolean {
        return isLogin.apply {
//            if(!this)
//                context.startActivity(Intent(context.context,LoginActivity::class.java).putExtra("loginCode",code))
        }
    }
    fun checkSignIn(context: RxAppCompatActivity, code:Int = 0): Boolean {
        return isLogin.apply {
//            if(!this)
//                context.startActivity(Intent(context,LoginActivity::class.java).putExtra("loginCode",code))
        }
    }
    private var _userModel: UserEntity? = null
        get() {
            field = field ?: gson.fromJson(getS(KEY_LOCAL_USER, ""), UserEntity::class.java)
            return field
        }

    val userModel = MutableLiveData<UserEntity?>()

    fun saveUserEntity(workerThread: Boolean = false, signOut: Boolean = false) {

        if (signOut) {
            _userModel = null
            add(KEY_LOCAL_USER, "")
        } else {
            gson.toJson(_userModel).also {
                add(KEY_LOCAL_USER, it)
            }
        }
        if (workerThread) {
            userModel.postValue(_userModel)
        } else {
            userModel.value = _userModel
        }

    }

    fun signin(token: String?, pf: UserEntity?) {
        _userModel = pf
        Account.token = token
        saveUserEntity()
/*        App.app?.apply {
            startActivity(Intent(this,MainActivity::class.java).clearTask().newTask())
        }*/
//        EventBus.getDefault().post(LoginSuccessEvent())
    }

    fun signout(code:Int = -1,workerThread: Boolean = false) {
        token = ""
        saveUserEntity(false, true)/*
        val intent = Intent(App.app, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("code",code)
        intent.putExtra("signOut", true)
        App.app?.startActivity(intent)*/
    }

    @Synchronized
    fun checkSignOut(r: HttpResultEntity<*>) {
/*        if (isLogin && r.code == ApiCode.CODE_TOKEN_INVALID||isLogin && r.code == ApiCode.CODE_ELSEWHERE_SIGNIN) {
            signout(true)
//            EventBus.getDefault().post(ElseWhereSigninEvent())
        }*/

    }

    fun add(key: String, value: Any): Boolean {
        return if (Account::preferences.isInitialized) {
            when (value) {
                is Boolean -> preferences.edit().putBoolean(key, value).apply()
                is Int -> preferences.edit().putInt(key, value).apply()
                is String -> preferences.edit().putString(key, value).apply()
                else -> {
                    throw IllegalArgumentException("暂不支持存储此数据类型：$value")
                }
            }
            true
        } else {
            throw RuntimeException("Account还没有绑定到Application")
        }
    }


    fun getB(key: String, default: Boolean = false): Boolean {
        return if (Account::preferences.isInitialized) {
            preferences.getBoolean(key, default)
        } else {
            throw RuntimeException("Account还没有绑定到Application")
        }
    }

    fun getI(key: String, default: Int = -1): Int {
        return if (Account::preferences.isInitialized) {
            preferences.getInt(key, default)
        } else {
            throw RuntimeException("Account还没有绑定到Application")
        }
    }

    fun getS(key: String, default: String = ""): String {
        return if (Account::preferences.isInitialized) {
            preferences.getString(key, default)
        } else {
            throw RuntimeException("Account还没有绑定到Application")
        }
    }


}
