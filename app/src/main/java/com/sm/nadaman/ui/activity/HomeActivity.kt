package com.sm.nadaman.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackflagbin.kcommon.base.BaseActivity
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.bean.Health
import com.sm.nadaman.common.db.HealthOpe
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.HomeContract
import com.sm.nadaman.mvp.presenter.HomePresenter
import com.sm.nadaman.ui.adapter.BannerViewHolder
import com.sm.nadaman.ui.adapter.EcgDataAdapter
import com.zhouwei.mzbanner.MZBannerView
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity<ApiService, CacheService, HomePresenter, Any?>(),
    HomeContract.IHomeView {

    val adapter: EcgDataAdapter by lazy {
        EcgDataAdapter(healthData).apply {
            setOnItemClickListener { adapter, view, position ->
                startActivity(if (Config.isSingleEcg)ReportResultActivity::class.java else ReportResult12Activity::class.java, Bundle().apply {
                    putLong("healthId", healthData[position].id)
                })
            }
        }
    }

    val list: ArrayList<Int> by lazy {
        ArrayList<Int>().apply {
            add(R.drawable.test1)
            add(R.drawable.test2)
            add(R.drawable.test3)
        }
    }

    val healthData: ArrayList<Health> by lazy {
        ArrayList<Health>().apply {
            addAll(
                if (Config.isSingleEcg)
                    HealthOpe.create().ecgHealthList
                else
                    HealthOpe.create().ecg12HealthList

            )
        }
    }

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.activity_home

    override val presenter: HomePresenter
        get() = HomePresenter(this)

    override fun initView() {
        super.initView()
        if (Config.isSingleEcg.not()){
            toolbar.setBackgroundResource(R.mipmap.toolbar_12)
            iv_ecg_logo.setImageResource(R.mipmap.home_ecg_logo_12)
        }
        (banner as MZBannerView<Int>).setPages(list) {
            BannerViewHolder()
        }
        ecg_measure.setOnClickListener {
            startActivity(EcgActivity::class.java, Bundle().apply {
                putInt("index", 0)
            })
        }
        ecg_calendar.setOnClickListener {
            startActivity(EcgActivity::class.java, Bundle().apply {
                putInt("index", 1)
            })
        }
        history_report.setOnClickListener {
            startActivity(EcgActivity::class.java, Bundle().apply {
                putInt("index", 1)
            })
        }
        friend_follow.setOnClickListener {
            startActivity(EcgActivity::class.java, Bundle().apply {
                putInt("index", 3)
            })
        }
        my_setting.setOnClickListener {
            startActivity(EcgActivity::class.java, Bundle().apply {
                putInt("index", 4)
            })
        }
        handle_guide.setOnClickListener {
            //todo
        }


        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HomeActivity.adapter
        }
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

    override fun onPause() {
        super.onPause()
        banner.pause()
    }

    override fun onResume() {
        super.onResume()
        banner.start()
    }

}
