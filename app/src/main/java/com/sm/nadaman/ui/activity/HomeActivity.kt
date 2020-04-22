package com.sm.nadaman.ui.activity

import android.graphics.Canvas
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
import androidx.recyclerview.widget.ItemTouchHelper
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.trello.rxlifecycle2.RxLifecycle.bindUntilEvent




class HomeActivity : BaseActivity<ApiService, CacheService, HomePresenter, Any?>(),
    HomeContract.IHomeView, OnItemSwipeListener {
    override fun clearView(p0: RecyclerView.ViewHolder?, p1: Int) {
    }

    override fun onItemSwiped(p0: RecyclerView.ViewHolder?, p1: Int) {
        HealthOpe.create().delHealth(healthData[p1])
    }

    override fun onItemSwipeStart(p0: RecyclerView.ViewHolder?, p1: Int) {

    }

    override fun onItemSwipeMoving(
        p0: Canvas?,
        p1: RecyclerView.ViewHolder?,
        p2: Float,
        p3: Float,
        p4: Boolean
    ) {

    }

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
        ArrayList<Health>()
    }
    private fun refershData(){
        healthData.clear()
        healthData.addAll(
            if (Config.isSingleEcg)
                HealthOpe.create().ecgHealthList
            else
                HealthOpe.create().ecg12HealthList

        )
        adapter.notifyDataSetChanged()
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
        val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(this);
        refershData()
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
        refershData()
        banner.start()
    }

}
