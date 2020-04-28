package com.sm.nadaman.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sm.nadaman.R

import com.blackflagbin.kcommon.base.BaseActivity
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.activity_look_ecg12_pic.*
import com.scwang.smartrefresh.layout.SmartRefreshLayout

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.LookEcg12PicContract
import com.sm.nadaman.mvp.presenter.LookEcg12PicPresenter
import com.sm.nadaman.common.db.HealthOpe
import com.sm.nadaman.common.widget.ecg.WaveViewConfig


class LookEcg12PicActivity : BaseActivity<ApiService, CacheService, LookEcg12PicPresenter, Any?>(),
    LookEcg12PicContract.ILookEcg12PicView {

    private var picId: Long = 0
    private val heartrate: Int = 0
    private val date: String? = null
    private val points: IntArray? = null
    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.activity_look_ecg12_pic

    override val presenter: LookEcg12PicPresenter
        get() = LookEcg12PicPresenter(this)

    override fun initView() {
        super.initView()
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val bundle = intent.extras
        picId = bundle.getLong("picId")
        val health = HealthOpe.create().getHealthById(picId)
        act_look_ecg_rl_report_pic.setListener {
            val config = WaveViewConfig()
            val titles = resources.getStringArray(R.array.ecgTitles)
            act_look_ecg_rl_report_pic.build(config.setSpeed(25f).setGain(10f), titles)
            val resultBitmap =
                act_look_ecg_rl_report_pic.getResultBitmap(
                    health.ecg12Data.data,
                    config,
                    titles
                )
            iv.setBitmap(resultBitmap)
            act_look_ecg_rl_report_pic.visibility = View.GONE
        }
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
