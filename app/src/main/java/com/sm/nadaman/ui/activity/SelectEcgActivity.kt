package com.sm.nadaman.ui.activity

import android.os.Bundle
import com.sm.nadaman.R
import com.trello.rxlifecycle2.components.RxActivity

class SelectEcgActivity:RxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectecg)
    }
}