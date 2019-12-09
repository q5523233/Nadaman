package com.blackflagbin.kcommon.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.transition.TransitionManager
import com.blackflagbin.kcommon.R
import com.blackflagbin.kcommon.facade.CommonLibrary
import com.blackflagbin.kcommon.http.HttpProvider
import com.blackflagbin.kcommon.utils.StatusBarUtil
import com.blackflagbin.kcommon.widget.ProgressDialog
import com.blankj.utilcode.util.SPUtils
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.toast


abstract class BaseActivity<out A, out C, P : IBasePresenter, in D> : RxAppCompatActivity(),
        IBaseView<D>, OnRefreshLoadMoreListener {


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
    private var mBundle: Bundle? = null
    private val MIN_DELAY_TIME = 300  // 两次点击间隔不能少于1000ms
    private var lastClickTime: Long = 0


    private val progressDialog: ProgressDialog? by lazy {
        ProgressDialog(this)
    }

    protected abstract val swipeRefreshView: SmartRefreshLayout?

    protected abstract val multiStateView: MultiStateView?

    protected abstract val layoutResId: Int

    protected abstract val presenter: P

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        mBundle = intent.extras
        mBundle?.let {
            onExtraBundleReceived(it)
        }
    }

    override fun onResume() {
        super.onResume()
        CommonLibrary.instance.onPageResumeListener?.onPageResume(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        //设置状态栏透明
        StatusBarUtil.immersive(this)
        CommonLibrary.instance.onPageCreateListener?.onPageCreate(this)
        initView()
        initData()
    }

    override fun onPause() {
        super.onPause()
        CommonLibrary.instance.onPagePauseListener?.onPagePause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        CommonLibrary.instance.onPageDestroyListener?.onPageDestroy(this)
    }

    //刷新
    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPresenter.initData(mDataMap)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
//        mPresenter.initData(mDataMap)
    }

    //跳转activity
    override fun startActivity(claz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, claz)
        bundle?.let { intent.putExtras(it) }
        startActivity(intent)
    }

    //结束activity
    override fun finishActivity() {
        finish()
    }

    //显示Toast
    override fun showTip(tipMsg: String) {
        toast(tipMsg).setGravity(Gravity.CENTER, 0, 0)
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
        multiStateView?.let{
//            TransitionManager.beginDelayedTransition(it)
            it?.viewState = MultiStateView.VIEW_STATE_CONTENT
        }
        swipeRefreshView?.setEnableRefresh(true)
        showContentView(data)
    }

    protected fun onReload(){
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

        swipeRefreshView?.setEnableRefresh(true)
        swipeRefreshView?.setEnableLoadMore(false)
        mMultiStateView?.viewState = MultiStateView.VIEW_STATE_CONTENT
        showTip(errorMsg)
/*        swipeRefreshView?.setEnableLoadMore(false)
        swipeRefreshView?.setEnableRefresh(false)
        mMultiStateView?.viewState = MultiStateView.VIEW_STATE_ERROR
        mTvErrorMsg?.text = errorMsg
        showTip(errorMsg)*/
    }

    //接收从上一个页面传递的参数
    protected open fun onExtraBundleReceived(bundle: Bundle) {}

    //初始化视图
    protected open fun initView() {
        setContentView(layoutResId)
        mBundle = intent.extras
        mBundle?.let {
            onExtraBundleReceived(it)
        }
        mPresenter = presenter
        mSwipeRefresh = swipeRefreshView
        mSwipeRefresh?.setOnRefreshListener(this)
        mMultiStateView = multiStateView
        mMultiStateView?.let {
            mEmptyView = it.getView(MultiStateView.VIEW_STATE_EMPTY)
//            mTvEmptyMsg = mEmptyView?.find(R.id.tv_empty_msg)
//            mBtEmptyRetry = mEmptyView?.find(R.id.bt_retry)
/*            mEmptyView?.setOnClickListener {
                mMultiStateView?.viewState = MultiStateView.VIEW_STATE_LOADING
                mPresenter.initData(mDataMap)
            }*/

            mErrorView = it.getView(MultiStateView.VIEW_STATE_ERROR)
            mTvErrorMsg = mErrorView?.find(R.id.tv_error_msg)
            mBtErrorRetry = mErrorView?.find(R.id.bt_retry)
            mBtErrorRetry?.setOnClickListener {
                mMultiStateView?.viewState = MultiStateView.VIEW_STATE_LOADING
                mPresenter.initData(mDataMap)
            }
        }
    }
    fun setEmptyText(text:String){
        mEmptyView?.find<TextView>(R.id.tv_empty_msg)?.text = text
    }


    // 点击空白区域 自动隐藏软键盘
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (null != this.currentFocus) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            val mInputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }

    //加载网络数据
    protected abstract fun initData()

    //显示加载数据成功后的内容
    protected abstract fun showContentView(data: D)

    private fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime >= MIN_DELAY_TIME) {
            flag = false
            lastClickTime = currentClickTime
        }
        return flag
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            if (isFastClick()) {
                return true
            }
        }

        return super.dispatchTouchEvent(ev)
    }
}
