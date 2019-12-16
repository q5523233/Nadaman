package com.sm.nadaman.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blackflagbin.kcommon.utils.StatusBarUtil
import com.sm.nadaman.R

class AccountActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.immersive(this)
        setContentView(R.layout.activity_account)

    }
}