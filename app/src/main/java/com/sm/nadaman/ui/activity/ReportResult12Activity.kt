package com.sm.nadaman.ui.activity

import com.blackflagbin.kcommon.base.BaseActivity
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.bean.Health
import com.sm.nadaman.common.db.HealthOpe
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.ReportResult12Contract
import com.sm.nadaman.mvp.presenter.ReportResult12Presenter
import kotlinx.android.synthetic.main.activity_report_result12.*


class ReportResult12Activity :
    BaseActivity<ApiService, CacheService, ReportResult12Presenter, Any?>(),
    ReportResult12Contract.IReportResult12View {

    private val pics = intArrayOf(
        R.mipmap.report_result_normal,
        R.mipmap.report_result_light,
        R.mipmap.report_result_mid,
        R.mipmap.report_result_strong
    )

    private val smilePics = intArrayOf(
        R.mipmap.report_result_smile_normal,
        R.mipmap.report_result_smile_light,
        R.mipmap.report_result_smile_mid,
        R.mipmap.report_result_smile_strong
    )

    private val health: Health by lazy {
        HealthOpe.create().getHealthById(intent.getLongExtra("healthId", -1))
    }

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.activity_report_result12

    override val presenter: ReportResult12Presenter
        get() = ReportResult12Presenter(this)

    override fun initView() {
        super.initView()
        tv_verdict.text = health.heartStrException
        iv_report_level.setImageResource(pics[health.exceptionLevel])
        iv_smile.setImageResource(smilePics[health.exceptionLevel])
        tv_report_result.text =
            resources.getStringArray(R.array.report_result)[health.exceptionLevel]
        tv_ave.text = "平均心率(bpm):${health.aveHeartRate}"
        tv_max.text = "最高心率(bpm):${health.maxHeartRate}"
        tv_min.text = "最低心率(bpm):${health.minHeartRate}"
        tv_duration.text = "测量时长:${health.measureDuration}秒"
        tv_time.text = "${health.date}"
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
