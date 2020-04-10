package com.sm.nadaman.ui.activity

import com.blackflagbin.kcommon.base.BaseActivity
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.bean.Health
import com.sm.nadaman.common.db.HealthOpe
import com.sm.nadaman.common.ecg.WaveInfo
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.common.stingToIntArr
import com.sm.nadaman.mvp.contract.ReportResultContract
import com.sm.nadaman.mvp.presenter.ReportResultPresenter
import kotlinx.android.synthetic.main.activity_report_result.*
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.timerTask

class ReportResultActivity : BaseActivity<ApiService, CacheService, ReportResultPresenter, Any?>(),
    ReportResultContract.IReportResultView {
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

    private val point: IntArray by lazy {
        stingToIntArr(health.mPoint, ",")
    }
    private var count = 0


    private val timer: Timer by lazy {
        Timer()
    }
    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.activity_report_result

    override val presenter: ReportResultPresenter
        get() = ReportResultPresenter(this)

    override fun initView() {
        super.initView()

        back_layout.post {
            back_layout.startDraw()
            layout_wave.initStyle()
            WaveInfo.getInstance().init(back_layout)
            WaveInfo.getInstance().setLayout(WaveInfo.WaveLayout.LAYOUT_12x1)
        }
        
        iv_report_level.setImageResource(pics[health.exceptionLevel])
        iv_smile.setImageResource(smilePics[health.exceptionLevel])
        tv_report_result.text =
            resources.getStringArray(R.array.report_result)[health.exceptionLevel]
        tv_ave.text = "平均心率(bpm):${health.aveHeartRate}"
        tv_max.text = "最高心率(bpm):${health.maxHeartRate}"
        tv_min.text = "最低心率(bpm):${health.minHeartRate}"
        tv_duration.text = "测量时长:${health.measureDuration}秒"
        tv_time.text = "${health.date}"
        iv_play.setOnClickListener {
            iv_play.isSelected = !iv_play.isSelected
        }
        startDraw()
    }

    private fun startDraw() {
        timer.schedule(timerTask {
            if (iv_play.isSelected) {
                if (count < point.size) {
                    layout_wave.length(point[count])
                    count++
                } else {
                    runOnUiThread {
                        iv_play.isSelected = false
                        toast("播放完毕")
                    }

                }

            }
        }, 100, 4)
    }

    override fun onDestroy() {
        timer.cancel()
        timer.purge()
        super.onDestroy()
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
