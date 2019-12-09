package com.sm.nadaman.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sm.nadaman.R
import com.sm.nadaman.common.Account
import com.sm.nadaman.common.utils.ThreadPoolUtil
import org.jetbrains.anko.startActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    }

    override fun onResume() {
        super.onResume()

        /*ThreadPoolUtil.getInstance().scheduleThread(Runnable {
            startActivity(
                Intent(
                    this,
                    if (Account.isLogin.not()) LoginActivity::class.java else MainActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            finish()
        })*/
        ThreadPoolUtil.getInstance().scheduleThread(Runnable {
            startActivity(Intent(this,WellComeActivity::class.java))
            finish()
        })

    }

}
