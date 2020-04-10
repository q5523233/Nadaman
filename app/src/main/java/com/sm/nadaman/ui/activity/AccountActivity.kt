package com.sm.nadaman.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blackflagbin.kcommon.utils.PermissionUtils
import com.blackflagbin.kcommon.utils.StatusBarUtil
import com.sm.nadaman.R

class AccountActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.immersive(this)
        setContentView(R.layout.activity_account)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}