package com.sm.nadaman.ui.fragment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import com.sm.nadaman.R

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.fragment_login_and_logon.*

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.LoginAndLogonContract
import com.sm.nadaman.mvp.presenter.LoginAndLogonPresenter
import org.jetbrains.anko.findOptional
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sm.nadaman.ui.adapter.LoginAndLogonFragmentAdapter

class LoginAndLogonFragment : BaseFragment<ApiService, CacheService, LoginAndLogonPresenter, Any?>(),
    LoginAndLogonContract.ILoginAndLogonView {

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_login_and_logon

    override val presenter: LoginAndLogonPresenter
        get() = LoginAndLogonPresenter(this)

    val adapter by lazy {
        LoginAndLogonFragmentAdapter(fragmentManager!!,list)
    }
    val list by lazy {
        ArrayList<Fragment>().apply {

        }
    }

    override fun initView() {
        super.initView()
        tv_terms.text = SpannableStringBuilder("登录表示你同意该软件的用户服务协议和隐私政策").apply {
            setSpan(ForegroundColorSpan(Color.parseColor("#5f5f5f")), 12, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                }
            },  12, 17, 0)
            setSpan(ForegroundColorSpan(Color.parseColor("#5f5f5f")),19, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                }
            }, 19, length, 0)
        }


    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
