package com.sm.nadaman.ui.fragment

import com.sm.nadaman.R

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import com.sm.nadaman.common.Config
import kotlinx.android.synthetic.main.fragment_register.*

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.RegisterContract
import com.sm.nadaman.mvp.presenter.RegisterPresenter

class RegisterFragment(val parent:LoginAndLogonFragment) : BaseFragment<ApiService, CacheService, RegisterPresenter, Any?>(),
    RegisterContract.IRegisterView {

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_register

    override val presenter: RegisterPresenter
        get() = RegisterPresenter(this)

    override fun initView() {
        super.initView()

        if (Config.isSingleEcg.not()) {
            et_moblie.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(R.mipmap.mobile_12),
                null,
                null,
                null
            )
            et_password.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(R.mipmap.password_12),
                null,
                null,
                null
            )
            et_name.setCompoundDrawablesWithIntrinsicBounds(
                resources.getDrawable(R.mipmap.name_12),
                null,
                null,
                null
            )
        }
        tv_confirm.setOnClickListener {
            parent.toScanBle()
        }
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
