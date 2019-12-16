package com.sm.nadaman.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object Config {
    private lateinit var preferences: SharedPreferences

    var isSingleEcg = true

    operator fun invoke(app: Application) {
        preferences =
            app.applicationContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    }
}