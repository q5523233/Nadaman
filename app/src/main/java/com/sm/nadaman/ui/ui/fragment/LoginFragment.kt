package com.sm.nadaman.ui.ui.fragment


import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import com.sm.nadaman.ui.mvp.contract.LoginContract
import com.sm.nadaman.ui.mvp.presenter.LoginPresenter
import com.sm.nadaman.R
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService

class LoginFragment : BaseFragment<ApiService, CacheService, LoginPresenter, Any?>(), LoginContract.ILoginView {

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_login

    override val presenter: LoginPresenter
        get() = LoginPresenter(this)

    override fun initView() {
        super.initView()
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
