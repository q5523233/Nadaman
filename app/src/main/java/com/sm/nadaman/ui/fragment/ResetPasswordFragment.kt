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
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.fragment_reset_password.*

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.ResetPasswordContract
import com.sm.nadaman.mvp.presenter.ResetPasswordPresenter
import org.jetbrains.anko.findOptional
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.finish

class ResetPasswordFragment : BaseFragment<ApiService, CacheService, ResetPasswordPresenter, Any?>(),
    ResetPasswordContract.IResetPasswordView {

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_reset_password

    override val presenter: ResetPasswordPresenter
        get() = ResetPasswordPresenter(this)

    override fun initView() {
        super.initView()
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

        tv_exit.setOnClickListener {
            finish()
        }

        cl_host.setBackgroundResource(if (Config.isSingleEcg) R.mipmap.bg_login_ecg1 else 0)
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
