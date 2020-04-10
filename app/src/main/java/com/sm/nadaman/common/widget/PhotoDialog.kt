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
import com.blackflagbin.kcommon.base.BaseActivity
import com.blackflagbin.kcommon.utils.PermissionUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sm.nadaman.R
import com.sm.nadaman.common.REQUEST_CODE_CHOOSE
import com.sm.nadaman.common.REQUEST_CODE_TAKE_PHOTO
import com.sm.nadaman.common.utils.FileProvider7
import com.sm.nadaman.common.utils.FileUtils
import com.sm.nadaman.common.utils.Glide4Engine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.dialog_add_photo.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoDialog(val mContext: Context) : BottomSheetDialog(mContext) {

    private var mCurrentPhotoPath: String? = null
    private var mOutputFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()


    }

    private fun init() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_photo, null)

        setContentView(view)

        tv_take_photo.setOnClickListener {
            dismiss()
            PermissionUtils.camera(mContext, {
                takePhoto()
            }, {})
        }
        tv_album.setOnClickListener {
            dismiss()
            PermissionUtils.readAndWrite(mContext, {
                openAlbum()
            }, {})
        }
        tv_cancel.setOnClickListener {
            dismiss()
        }

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.attributes.gravity = Gravity.BOTTOM
/*
        val parent = view.parent as ViewGroup
        parent.setBackgroundResource(android.R.color.transparent)*/
    }

    /**
     * 打开相册
     */
    private fun openAlbum() {
        if (mContext is BaseActivity<*, *, *, *>) {
            Matisse.from(mContext)
                .choose(MimeType.ofImage(), false)
                .showSingleMediaType(true)
                .countable(false)
                .capture(false)
                .maxSelectable(1)
                .gridExpectedSize(mContext.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(Glide4Engine())    // for glide-V4
                .originalEnable(false)
                .maxOriginalSize(10)
                .setQuickSelect(true)
                .forResult(REQUEST_CODE_CHOOSE)
        }
    }


    /**
     * 拍照
     */
    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(mContext.packageManager) != null) {
            val filename = "Nadaman_" + SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.ENGLISH
            ).format(Date()) + ".jpg"
            mCurrentPhotoPath = FileUtils.createDir(mContext, FileUtils.images) + filename
            mOutputFileUri = FileProvider7.getUriForFile(mContext, File(mCurrentPhotoPath))
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri)
            (mContext as BaseActivity<*, *, *, *>).startActivityForResult(
                intent,
                REQUEST_CODE_TAKE_PHOTO
            )
        }
    }

    fun getTakePhoto(): String? {
        return mCurrentPhotoPath
    }

}