package com.sm.nadaman.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.sm.nadaman.common.ecg.Config
import com.sm.nadaman.common.net.UserEntity
import com.sm.nadaman.common.widget.SettingWindow

object Config {
    private lateinit var preferences: SharedPreferences

    var isSingleEcg = true

    operator fun invoke(app: Application) {
        preferences =
            app.applicationContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    }

    var config: SettingWindow.ConfigBean? = null
        get() {
            field = field ?: gson.fromJson(Account.getS("Config", ""), SettingWindow.ConfigBean::class.java)
            if (field != null){
                gson.toJson(field).also {
                    Account.add("Config", it)
                }
            }
            return field
        }
}