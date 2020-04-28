package com.sm.nadaman.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sm.nadaman.R

import com.blackflagbin.kcommon.base.BaseActivity
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout

import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.LookEcgPicContract
import com.sm.nadaman.mvp.presenter.LookEcgPicPresenter
import kotlinx.android.synthetic.main.activity_look_ecg_pic.*


class LookEcgPicActivity : BaseActivity<ApiService, CacheService, LookEcgPicPresenter, Any?>(),
    LookEcgPicContract.ILookEcgPicView {

    private var picId: Long = 0
    private var heartrate: Int = 0
    private var date: String? = null
    private var points: IntArray? = null

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.activity_look_ecg_pic

    override val presenter: LookEcgPicPresenter
        get() = LookEcgPicPresenter(this)

    override fun initView() {
        super.initView()
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val bundle = intent.extras
        picId = bundle.getLong("picId")
        heartrate = bundle.getInt("hr")
        date = bundle.getString("date")
        points = bundle.getIntArray("points")
        act_look_ecg_tv_report_pic_id.text = picId.toString()
        act_look_ecg_tv_hr.text = heartrate.toString()
        act_look_ecg_tv_report_pic_time.text = "检测时间：$date"
        act_look_ecg_my_view.initData(points)

    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
