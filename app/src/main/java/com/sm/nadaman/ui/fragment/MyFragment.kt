package com.sm.nadaman.ui.fragment

import com.sm.nadaman.R

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.db.manager.UserDBController

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.MyContract
import com.sm.nadaman.mvp.presenter.MyPresenter
import com.sm.nadaman.ui.activity.DisclaimerActivity
import com.sm.nadaman.ui.activity.EcgActivity
import com.sm.nadaman.ui.activity.HandleActivity
import com.techne.nomnompos.app.App
import kotlinx.android.synthetic.main.activity_ecg.*
import kotlinx.android.synthetic.main.fragment_my.*

class MyFragment : BaseFragment<ApiService, CacheService, MyPresenter, Any?>(), MyContract.IMyView {

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_my

    override val presenter: MyPresenter
        get() = MyPresenter(this)

    override fun initView() {
        super.initView()

        if (Config.isSingleEcg.not()){
            toolbar.setBackgroundResource(R.mipmap.toolbar_12)
        }

        tv_history.setOnClickListener {
            (activity as EcgActivity).tab.getTabAt(1)?.select()
        }

        tv_handle_guide.setOnClickListener {
            startActivity(HandleActivity::class.java)

        }

        tv_disclaimer.setOnClickListener {
            startActivity(DisclaimerActivity::class.java)

        }

        tv_name.text = UserDBController.getInstance().currentUser.userName
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
