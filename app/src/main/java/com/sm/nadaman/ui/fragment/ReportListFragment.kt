package com.sm.nadaman.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.bean.Health
import com.sm.nadaman.common.db.HealthOpe
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.ReportListContract
import com.sm.nadaman.mvp.presenter.ReportListPresenter
import com.sm.nadaman.ui.activity.ReportResult12Activity
import com.sm.nadaman.ui.activity.ReportResultActivity
import com.sm.nadaman.ui.adapter.EcgDataAdapter
import kotlinx.android.synthetic.main.fragment_report_list.recycler
import kotlinx.android.synthetic.main.fragment_report_list.toolbar

class ReportListFragment : BaseFragment<ApiService, CacheService, ReportListPresenter, Any?>(),
    ReportListContract.IReportListView {

    private val healthOpe: HealthOpe by lazy {
        HealthOpe.create()
    }
    private val list by lazy {
        ArrayList<Health>().apply {
            addAll(
                healthOpe.getUserHealthListFromDate(
                    ReportListFragmentArgs.fromBundle(arguments).date,
                    if (Config.isSingleEcg)
                        0
                    else
                        1
                )
            )
        }
    }

    val adapter by lazy {
        EcgDataAdapter(list).apply {
            setOnItemClickListener { adapter, view, position ->
                startActivity(if (Config.isSingleEcg)ReportResultActivity::class.java else ReportResult12Activity::class.java, Bundle().apply {
                    putLong("healthId", list[position].id)
                })
            }
        }
    }
    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_report_list

    override val presenter: ReportListPresenter
        get() = ReportListPresenter(this)

    override fun initView() {
        super.initView()
        toolbar.setNavigationOnClickListener {
            findNavController1()?.navigateUp()
        }
        if (Config.isSingleEcg.not()){
            toolbar.setBackgroundResource(R.mipmap.toolbar_12)
        }
        recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@ReportListFragment.adapter
        }
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
