package com.sm.nadaman.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sm.nadaman.R

import com.blackflagbin.kcommon.base.BaseActivity
import com.davemorrissey.labs.subscaleview.ImageSource
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.common.Config

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.HandleContract
import com.sm.nadaman.mvp.presenter.HandlePresenter
import kotlinx.android.synthetic.main.activity_handle.*
import kotlinx.android.synthetic.main.activity_handle.toolbar
import kotlinx.android.synthetic.main.fragment_calendar.*


class HandleActivity : BaseActivity<ApiService, CacheService, HandlePresenter, Any?>(),
    HandleContract.IHandleView {

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.activity_handle

    override val presenter: HandlePresenter
        get() = HandlePresenter(this)

    override fun initView() {
        super.initView()
        if (Config.isSingleEcg.not()) {
            toolbar.setBackgroundResource(R.mipmap.toolbar_12)
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
        imageView.setImage(ImageSource.resource(if (Config.isSingleEcg) R.drawable.handle_1 else R.drawable.handle_12))
        imageView.setDoubleTapZoomScale(2f)
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
