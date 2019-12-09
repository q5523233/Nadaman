package com.blackflagbin.kcommon.utils

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ToastUtils

/**
 *  Description :动态权限管理
 */
object PermissionUtils {
    private val RESULT_CODE_TAKE_CAMERA = 7461    //拍照
    private val RESULT_CODE_OPEN_ALBUM = 7462     //读写
    private val RESULT_CODE_SOUND_RECORD = 7463   //录音
    private val RESULT_CODE_READ_PHONE_STATE = 7465   //设备信息
    private val RESULT_CODE_LOCATION = 7466   //定位

    private var cameraCallback: (() -> Unit)? = null        //相机回调
    private var readAndWriteCallback: (() -> Unit)? = null  //读写回调
    private var audioCallback: (() -> Unit)? = null         //录音回调
    private var phoneStateCallback: (() -> Unit)? = null         //设备信息
    private var locationCallback: (() -> Unit)? = null         //定位
    private var failCallback: (() -> Unit)? = {
        ToastUtils.showShort("Lose permission may prevent the app working properly.")
    }         //失败

    /**
     * 相机权限申请
     */
    fun camera(context: Context, cameraCallback: () -> Unit, fail: (() -> Unit)? = failCallback) {
        PermissionUtils.cameraCallback = cameraCallback
        PermissionUtils.failCallback = fail
        permission(
            context,
            Manifest.permission.CAMERA,
            RESULT_CODE_TAKE_CAMERA,
            cameraCallback
        )
    }

    /**
     * 读写权限申请
     */
    fun readAndWrite(context: Context, readAndWriteCallback: () -> Unit, fail: (() -> Unit)? = failCallback) {
        PermissionUtils.readAndWriteCallback = readAndWriteCallback
        PermissionUtils.failCallback = fail
        permission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            RESULT_CODE_OPEN_ALBUM,
            readAndWriteCallback
        )
    }

    /**
     * 录音权限申请
     */
    fun audio(context: Context, audioCallback: () -> Unit, fail: (() -> Unit)? = failCallback) {
        PermissionUtils.audioCallback = audioCallback
        PermissionUtils.failCallback = fail
        permission(
            context,
            Manifest.permission.RECORD_AUDIO,
            RESULT_CODE_SOUND_RECORD,
            audioCallback
        )
    }
    /**
     * 手机状态权限申请
     */
    fun phoneState(context: Context, phoneStateCallback: () -> Unit, fail: (() -> Unit)? = failCallback) {
        PermissionUtils.phoneStateCallback = phoneStateCallback
        PermissionUtils.failCallback = fail
        permission(
            context,
            Manifest.permission.READ_PHONE_STATE,
            RESULT_CODE_READ_PHONE_STATE,
            phoneStateCallback
        )
    }
    /**
     * 定位权限
     */
    fun location(context: Context, locationCallback: () -> Unit, fail: (() -> Unit)? = null) {
        PermissionUtils.locationCallback = locationCallback
        PermissionUtils.failCallback = fail
        permission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            RESULT_CODE_LOCATION,
            locationCallback
        )
    }

    /**
     * 权限申请结果
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        when (requestCode) {
            RESULT_CODE_TAKE_CAMERA -> {    //拍照
                if (accepted) {
                    cameraCallback?.let { it() }
                } else {
                    //用户拒绝
                    failCallback?.let { it() }
                }
            }
            RESULT_CODE_OPEN_ALBUM -> { //读写
                if (accepted) {
                    readAndWriteCallback?.let { it() }
                } else {
                    failCallback?.let { it() }
                }
            }
            RESULT_CODE_SOUND_RECORD -> { //录音
                if (accepted) {
                    audioCallback?.let { it() }
                } else {
                    failCallback?.let { it() }
                }
            }
            RESULT_CODE_READ_PHONE_STATE -> { //设备信息
                if (accepted) {
                    phoneStateCallback?.let { it() }
                } else {
                    failCallback?.let { it() }
                }
            }
            RESULT_CODE_LOCATION -> { //定位
                if (accepted) {
                    locationCallback?.let { it() }
                } else {
                    failCallback?.let { it() }
                }
            }
        }
    }

    //权限申请
    private fun permission(context: Context, systemCode: String, resultCode: Int, callback: () -> Unit) {
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(context, systemCode) == PackageManager.PERMISSION_GRANTED) {
            callback()
        } else {
            //申请权限
            ActivityCompat.requestPermissions(context as AppCompatActivity, arrayOf(systemCode), resultCode)
        }
    }
}