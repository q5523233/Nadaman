package com.sm.nadaman.common.widget

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sm.nadaman.R
import kotlinx.android.synthetic.main.dialog_reset_password.*

class ResetPasswordDialog(val mContext: Context,val callBack: () -> Unit) : BottomSheetDialog(mContext) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()


    }

    private fun init() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_reset_password, null)

        setContentView(view)

        tv_confirm.setOnClickListener {
            callBack.invoke()
            dismiss()
        }

        tv_cancel.setOnClickListener {
            dismiss()
        }

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.attributes.gravity = Gravity.BOTTOM

        val parent = view.parent as ViewGroup
        parent.setBackgroundResource(android.R.color.transparent)
    }

}