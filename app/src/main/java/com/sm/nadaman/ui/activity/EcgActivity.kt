package com.sm.nadaman.ui.activity

import android.content.Intent
import android.os.Bundle
import com.blackflagbin.kcommon.base.BaseActivity
import com.blackflagbin.kcommon.utils.PermissionUtils
import com.google.android.material.tabs.TabLayout
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.event.onActivityResultEvent
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.common.utils.FragmentBottomNavigation
import com.sm.nadaman.mvp.contract.EcgContract
import com.sm.nadaman.mvp.presenter.EcgPresenter
import kotlinx.android.synthetic.main.activity_ecg.*
import org.greenrobot.eventbus.EventBus


class EcgActivity : BaseActivity<ApiService, CacheService, EcgPresenter, Any?>(),
    EcgContract.IEcgView, TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.activity_ecg

    override val presenter: EcgPresenter
        get() = EcgPresenter(this)

    private var lastPosition = 0

    private val fragmentBottomNavigation: FragmentBottomNavigation by lazy {
        FragmentBottomNavigation(
            supportFragmentManager,
            "frg",
            R.id.fragment,
            intent.getIntExtra("index", 0)
        )
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        //防止重複調用導航
        if (lastPosition == p0?.position) {
            return
        }
        when (p0?.position) {
            0, 1, 3, 4 -> {
                fragmentBottomNavigation.switchFragment(p0.position)
                lastPosition = p0.position
            }
            2 -> finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBottomNavigation.onCreate(savedInstanceState)
        savedInstanceState?.let {
            tab.getTabAt(fragmentBottomNavigation.mCurrentIndex)?.select()
        }
    }

    private fun initTab() {
        tab.setTabTextColors(resources.getColor(R.color.CACACA),resources.getColor(R.color.ecg12_color))

        tab.addTab(
            tab.newTab().setIcon(if (Config.isSingleEcg) R.drawable.tab_ecg_measure else R.drawable.tab_ecg_measure_12).setText(
                "测量"
            )
        )
        tab.addTab(
            tab.newTab().setIcon(if (Config.isSingleEcg) R.drawable.tab_report else R.drawable.tab_report_12).setText(
                R.string.report
            )
        )
        tab.addTab(tab.newTab().setIcon(if (Config.isSingleEcg) R.drawable.tab_home else R.drawable.tab_home_12))
        tab.addTab(
            tab.newTab().setIcon(if (Config.isSingleEcg) R.drawable.tab_follow else R.drawable.tab_follow_12).setText(
                R.string.follow
            )
        )
        tab.addTab(
            tab.newTab().setIcon(if (Config.isSingleEcg) R.drawable.tab_mysetting else R.drawable.tab_mysetting_12).setText(
                R.string.mind
            )
        )
        tab.getTabAt(intent.getIntExtra("index", 0))?.select()
        tab.addOnTabSelectedListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EventBus.getDefault().post(onActivityResultEvent(requestCode, resultCode, data))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragmentBottomNavigation.onSaveInstanceState(outState)
    }

    override fun initView() {
        super.initView()
        initTab()
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
