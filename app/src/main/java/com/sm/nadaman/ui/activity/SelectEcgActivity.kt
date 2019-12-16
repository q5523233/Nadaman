package com.sm.nadaman.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blackflagbin.kcommon.utils.StatusBarUtil
import com.sm.nadaman.R
import com.sm.nadaman.common.Config
import kotlinx.android.synthetic.main.activity_selectecg.*

class SelectEcgActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.immersive(this)
        setContentView(R.layout.activity_selectecg)
        rg_ecg.setOnCheckedChangeListener { group, checkedId ->
            Config.isSingleEcg = checkedId == R.id.rb_ecg1
        }
    }
}