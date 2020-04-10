package com.sm.nadaman.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.bean.Friend
import com.sm.nadaman.common.db.FriendOpe
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.common.navigate1
import com.sm.nadaman.mvp.contract.FollowContract
import com.sm.nadaman.mvp.presenter.FollowPresenter
import com.sm.nadaman.ui.adapter.FollowAdapter
import kotlinx.android.synthetic.main.fragment_follow.*

class FollowFragment : BaseFragment<ApiService, CacheService, FollowPresenter, Any?>(),
    FollowContract.IFollowView {


    val list by lazy {
        ArrayList<Friend>().apply {
            addAll(FriendOpe.create().list)
        }
    }

    val adapter: FollowAdapter by lazy {
        FollowAdapter(list).apply {
            setOnItemClickListener { _, _, position ->
                findNavController1()?.navigate1(
                    FollowFragmentDirections.actionFollowFragmentToAddFollowFragment().setIsEdit(
                        true
                    ).apply {
                        arguments.putSerializable("data", list[position])
                    })
            }
        }
    }

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = multi_state_view

    override val layoutResId: Int
        get() = R.layout.fragment_follow

    override val presenter: FollowPresenter
        get() = FollowPresenter(this)

    override fun initView() {
        super.initView()

        if (Config.isSingleEcg.not()){
            toolbar.setBackgroundResource(R.mipmap.toolbar_12)
        }
        recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = this@FollowFragment.adapter
        }
        iv_add.setOnClickListener {
            findNavController1()?.navigate1(FollowFragmentDirections.actionFollowFragmentToAddFollowFragment())
        }

        if (list.isEmpty()) {
            showEmptyView()
        } else {
            showSuccessView(list)
        }
    }

    override fun onFragmentShow() {
        super.onFragmentShow()
        list.clear()
        list.addAll(FriendOpe.create().list)
        adapter.notifyDataSetChanged()

        if (list.isEmpty()) {
            showEmptyView()
        } else {
            showSuccessView(list)
        }
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

}
