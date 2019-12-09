package com.blackflagbin.kcommon.http

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.blackflagbin.kcommon.base.IBaseView
import com.blackflagbin.kcommon.entity.net.IApiException
import com.blackflagbin.kcommon.facade.CommonLibrary

object ErrorHandler {

    fun handleError(e: Throwable, baseView: IBaseView<*>) {
        var context: Activity? = null
        if (baseView is AppCompatActivity) {
            context = baseView
        }
        if (baseView is Fragment) {
            context = (baseView as Fragment).activity
        }
        if (e is IApiException) {
//            val finalContext = context
//            Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe {
////                Toast.makeText(
////                        finalContext, e.msg, Toast.LENGTH_SHORT).show()
//            }

            val resultCode = (e as IApiException).resultCode
//            baseView.showErrorView((e as IApiException).msg)
            CommonLibrary.instance.errorHandleMap?.map {
                if (it.key == resultCode) {
                    it.value.invoke(e)
                }
            }
        }
    }

    /**
     * 跳转到登录页面，同时清空之前的任务栈
     *
     * @param context    context
     * @param loginClazz 登录页面Activity的Class类
     */
    private fun startLoginActivity(context: Context, loginClazz: Class<*>) {
        context.startActivity(
                Intent(
                        context,
                        loginClazz).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
