package com.sm.nadaman.ui.activity

import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_report_result.*
import kotlinx.android.synthetic.main.activity_report_result12.*
import kotlinx.android.synthetic.main.activity_report_result12.iv_report_level
import kotlinx.android.synthetic.main.activity_report_result12.iv_smile
import kotlinx.android.synthetic.main.activity_report_result12.toolbar
import kotlinx.android.synthetic.main.activity_report_result12.tv_ave
import kotlinx.android.synthetic.main.activity_report_result12.tv_duration
import kotlinx.android.synthetic.main.activity_report_result12.tv_max
import kotlinx.android.synthetic.main.activity_report_result12.tv_min
import kotlinx.android.synthetic.main.activity_report_result12.tv_report_result
import kotlinx.android.synthetic.main.activity_report_result12.tv_time


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
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tv_verdict.text = health.heartStrException
        iv_report_level.setImageResource(pics[health.exceptionLevel.div(2)])
        iv_smile.setImageResource(smilePics[health.exceptionLevel.div(2)])
        tv_report_result.text =
            resources.getStringArray(R.array.report_result)[health.exceptionLevel.div(2)]
        tv_ave.text = "平均心率(bpm):${health.aveHeartRate}"
        tv_max.text = "最高心率(bpm):${health.maxHeartRate}"
        tv_min.text = "最低心率(bpm):${health.minHeartRate}"
        tv_duration.text = "测量时长:${health.measureDuration}秒"
        tv_time.text = "${health.date}"
        iv_playback.setOnClickListener {

            val bundle = Bundle()
            bundle.putLong("picId", health.id)
            bundle.putInt("hr", health.aveHeartRate)
            bundle.putString("date", health.date)
//                bundle.putIntArray("points", point);
            startActivity(LookEcg12PicActivity::class.java, bundle)
        }
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
