package com.blackflagbin.kcommon.widget

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.blackflagbin.kcommon.R

class ProgressDialog(val context: Context) {
    val progressDialog: Dialog by lazy {
        Dialog(context,R.style.progress_dialog).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    fun show(){
        if(!progressDialog.isShowing)
            progressDialog.show()
    }

    fun dissmiss(){

        if(progressDialog.isShowing)
            progressDialog.dismiss()
    }
}