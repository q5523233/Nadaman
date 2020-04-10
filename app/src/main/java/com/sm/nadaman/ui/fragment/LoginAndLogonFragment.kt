package com.sm.nadaman.ui.fragment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.sm.nadaman.R

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.fragment_login_and_logon.*

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.LoginAndLogonContract
import com.sm.nadaman.mvp.presenter.LoginAndLogonPresenter
import org.jetbrains.anko.findOptional
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.navigate1
import com.sm.nadaman.ui.adapter.LoginAndLogonFragmentAdapter
import org.jetbrains.anko.forEachChild

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
            add(RegisterFragment(this@LoginAndLogonFragment))
            add(LoginFragment(this@LoginAndLogonFragment))
        }
    }

    override fun initView() {
        super.initView()
        if (Config.isSingleEcg.not()){
            tablayout.setTabTextColors(resources.getColor(R.color.ff9f9f9f),resources.getColor(R.color.ecg12_color))
            tablayout.setSelectedTabIndicatorColor(resources.getColor(R.color.ecg12_color))
        }
        tv_terms.text = SpannableStringBuilder("登录表示你同意该软件的用户服务协议和隐私政策").apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }
            },  11, 17, 0)
            setSpan(ForegroundColorSpan(Color.parseColor("#5f5f5f")), 11, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }
            }, 18, length, 0)
            setSpan(ForegroundColorSpan(Color.parseColor("#5f5f5f")),18, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
        viewpage.adapter = adapter
        tablayout.setupWithViewPager(viewpage)

        tablayout.getTabAt(0)?.text = getString(R.string.logon)
        tablayout.getTabAt(1)?.text = getString(R.string.login)

        cl_host.setBackgroundResource(if (Config.isSingleEcg) R.mipmap.bg_login_ecg1 else R.mipmap.bg_login_ecg12)

    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun toReset() {
        findNavController1()?.navigate1(LoginAndLogonFragmentDirections.actionLoginAndLogonFragmentToResetPasswordFragment())
    }


    fun toScanBle() {
        findNavController1()?.navigate1(LoginAndLogonFragmentDirections.actionLoginAndLogonFragmentToScanBleFragment())
    }

}
