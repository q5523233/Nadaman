package com.sm.nadaman.ui.fragment


import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.common.widget.ResetPasswordDialog
import com.sm.nadaman.mvp.contract.LoginContract
import com.sm.nadaman.mvp.presenter.LoginPresenter
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment(val parent: LoginAndLogonFragment) :
    BaseFragment<ApiService, CacheService, LoginPresenter, Any?>(), LoginContract.ILoginView {

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
            iv_error_password.setImageResource(R.mipmap.error_input_password_12)
        }
        tv_reset_password.setOnClickListener {
            ResetPasswordDialog(context!!) {
                parent.toReset()
            }.show()
        }
        tv_confirm.setOnClickListener {
            parent.toScanBle()
        }
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
