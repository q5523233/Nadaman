package com.blackflagbin.kcommon.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.blackflagbin.kcommon.R
import com.blackflagbin.kcommon.facade.CommonLibrary
import com.blackflagbin.kcommon.http.HttpProvider
import com.blackflagbin.kcommon.widget.ProgressDialog
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SPUtils
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.trello.rxlifecycle2.components.support.RxFragment
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.toast

/**
 * Created by blackflagbin on 2017/6/28.
 */


abstract class BaseFragment<out A, out C, P : IBasePresenter, in D> : RxFragment(), IBaseView<D>,
    OnRefreshLoadMoreListener {

    protected lateinit var mPresenter: P
    protected val mSPUtils: SPUtils by lazy { SPUtils.getInstance(CommonLibrary.instance.spName) }
    protected val mApiService: A by lazy { HttpProvider.instance.provideApiService<A>() }
    protected val mCacheService: C by lazy { HttpProvider.instance.provideCacheService<C>() }
    protected val mDataMap: HashMap<String, String> by lazy { hashMapOf<String, String>() }
    protected var mSwipeRefresh: SmartRefreshLayout? = null
    protected var mMultiStateView: MultiStateView? = null
    protected var mErrorView: View? = null
    protected var mTvErrorMsg: TextView? = null
    protected var mBtErrorRetry: Button? = null
    protected var mEmptyView: View? = null
    protected var mTvEmptyMsg: TextView? = null
    protected var mBtEmptyRetry: Button? = null
    protected lateinit var mRootView: View

    private var isWaitingForOnFragmentResume = false

    protected abstract val swipeRefreshView: SmartRefreshLayout?

    protected abstract val multiStateView: MultiStateView?

    protected abstract val layoutResId: Int

    protected abstract val presenter: P


    private val progressDialog: ProgressDialog? by lazy {
        ProgressDialog(activity!!)
    }

    override fun onResume() {
        super.onResume()
        if (isWaitingForOnFragmentResume) {
            isWaitingForOnFragmentResume = false
            onFragmentVisible()
        }
        activity?.let {
            CommonLibrary.instance.onPageResumeListener?.onPageResume(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        mRootView = inflater.inflate(layoutResId, container, false)
        mPresenter = presenter
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            CommonLibrary.instance.onPageCreateListener?.onPageCreate(it)
        }
        initView()
        initData()
    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            CommonLibrary.instance.onPagePauseListener?.onPagePause(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.let {
            CommonLibrary.instance.onPageDestroyListener?.onPageDestroy(it)
        }
    }

    override fun onStop() {
        super.onStop()
        KeyboardUtils.hideSoftInput(mRootView)
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPresenter.initData(mDataMap)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            KeyboardUtils.hideSoftInput(mRootView)
            onFragmentHide()
        } else {
            KeyboardUtils.hideSoftInput(mRootView)
            onFragmentShow()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            if (isResumed) {
                KeyboardUtils.hideSoftInput(mRootView)
                onFragmentVisible()
            } else {
                isWaitingForOnFragmentResume = true
            }

        } else {
            if (isResumed) {
                KeyboardUtils.hideSoftInput(mRootView)
                onFragmentInvisible()
            }
        }
    }

    protected open fun initView() {
        mPresenter = presenter
        mSwipeRefresh = swipeRefreshView
        mSwipeRefresh?.setOnRefreshLoadMoreListener(this)
        mMultiStateView = multiStateView
        mMultiStateView?.let {
            mEmptyView = it.getView(MultiStateView.VIEW_STATE_EMPTY)
            mTvEmptyMsg = mEmptyView?.findOptional(R.id.tv_empty_msg)
            mBtEmptyRetry = mEmptyView?.findOptional(R.id.bt_retry)
            mEmptyView?.setOnClickListener {
                onEmptyClick()
                /*mMultiStateView?.viewState = MultiStateView.VIEW_STATE_LOADING
                mPresenter.initData(mDataMap)*/
            }

            mErrorView = it.getView(MultiStateView.VIEW_STATE_ERROR)
            mTvErrorMsg = mErrorView?.findOptional(R.id.tv_error_msg)
            mBtErrorRetry = mErrorView?.findOptional(R.id.bt_retry)
            mErrorView?.setOnClickListener {
                onReload()
                initData()
            }
        }
    }

    protected fun setEmptyText(text: String) {
        mTvEmptyMsg?.text = text
    }

    protected open fun onEmptyClick() {

    }

    override fun startActivity(claz: Class<*>, bundle: Bundle?) {
        val intent = Intent(activity, claz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun findNavController1(): NavController? {
        try {
            return findNavController()
        } catch (e: Exception) {
            return null
        }

    }

    override fun finishActivity() {
        activity?.finish()
    }

    override fun showTip(tipMsg: String) {
        context?.toast(tipMsg)?.setGravity(Gravity.CENTER, 0, 0)
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        swipeRefreshView?.finishRefresh()
        swipeRefreshView?.finishLoadMore()
        progressDialog?.dissmiss()
    }

    override fun showSuccessView(data: D) {
        multiStateView?.let {
            it?.viewState = MultiStateView.VIEW_STATE_CONTENT
        }
        swipeRefreshView?.setEnableRefresh(true)
        showContentView(data)
    }

    protected fun onReload() {
        swipeRefreshView?.setEnableLoadMore(false)
        swipeRefreshView?.setEnableRefresh(false)
        mMultiStateView?.viewState = MultiStateView.VIEW_STATE_LOADING
    }

    override fun showEmptyView() {
        swipeRefreshView?.setEnableLoadMore(false)
        swipeRefreshView?.setEnableRefresh(false)
        mMultiStateView?.viewState = MultiStateView.VIEW_STATE_EMPTY
    }

    override fun showErrorView(errorMsg: String) {
/*        swipeRefreshView?.setEnableLoadMore(false)
        swipeRefreshView?.setEnableRefresh(false)
        mMultiStateView?.viewState = MultiStateView.VIEW_STATE_ERROR
        mTvErrorMsg?.text = errorMsg*/

        swipeRefreshView?.setEnableRefresh(true)
        swipeRefreshView?.setEnableLoadMore(false)
        mMultiStateView?.viewState = MultiStateView.VIEW_STATE_CONTENT
        showTip(errorMsg)
    }

    // FragmentTransaction 调用show 回调
    protected open fun onFragmentShow() {

    }

    // FragmentTransaction 调用hide 回调
    protected open fun onFragmentHide() {

    }

    /**
     * viewPager中界面每次可见调用；
     */
    protected open fun onFragmentVisible() {

    }

    /**
     * viewPager中界面每次不可见调用；
     */
    protected open fun onFragmentInvisible() {

    }

    protected abstract fun initData()

    protected abstract fun showContentView(data: D)

}
