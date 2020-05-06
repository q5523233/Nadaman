package com.sm.nadaman.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sm.nadaman.R

import com.blackflagbin.kcommon.base.BaseActivity
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.common.Config

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.DisclaimerContract
import com.sm.nadaman.mvp.presenter.DisclaimerPresenter
import kotlinx.android.synthetic.main.activity_disclaimer.*
import kotlinx.android.synthetic.main.activity_disclaimer.toolbar
import kotlinx.android.synthetic.main.activity_handle.*

class DisclaimerActivity : BaseActivity<ApiService, CacheService, DisclaimerPresenter, Any?>(),
    DisclaimerContract.IDisclaimerView {

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.activity_disclaimer

    override val presenter: DisclaimerPresenter
        get() = DisclaimerPresenter(this)

    override fun initView() {
        super.initView()
        if (Config.isSingleEcg.not()) {
            toolbar.setBackgroundResource(R.mipmap.toolbar_12)
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
        webview.loadUrl("file:///android_asset/disclaimer.html");
    }

    override fun onDestroy() {
        webview.destroy()
        webview.removeAllViews()
        super.onDestroy()
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
