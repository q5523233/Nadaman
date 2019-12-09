package com.blackflagbin.kcommon.http.subscribers


import com.blackflagbin.kcommon.base.IBaseRefreshAndLoadMoreView
import com.blackflagbin.kcommon.base.IBaseView
import com.blackflagbin.kcommon.entity.net.ApiException
import com.blackflagbin.kcommon.http.ErrorHandler
import io.reactivex.observers.ResourceObserver
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NoProgressObserver<T, V>(
    private val mBaseView: IBaseView<V>,
    private val mCallBack: ObserverCallBack<T> = object : ObserverCallBack<T> {
        override fun onNext(t: T) {

        }

        override fun onError(e: Throwable) {

        }

    },
    private val mIsLoadMore: Boolean = false
) : ResourceObserver<T>() {

    override fun onNext(t: T) {
        if (mIsLoadMore) {
            if (mBaseView is IBaseRefreshAndLoadMoreView) {
                mBaseView.afterLoadMore(t as V)
            }
        }
        mBaseView.dismissLoading()
        mCallBack.onNext(t)
    }

    override fun onError(e: Throwable) {
        ErrorHandler.handleError(e, mBaseView)

        if (mIsLoadMore) {
            if (mBaseView is IBaseRefreshAndLoadMoreView<*>) {
                mBaseView.afterLoadMoreError(e)
            }
        } else {
            mBaseView.dismissLoading()
            if (e is ApiException) {
                when (e.resultCode) {
                    90000, 90001, 89001, 89002, 89003 -> return@onError
                }
                mCallBack.onError(Throwable(e.msg, e))
            } else {
                when (e) {
                    is SocketTimeoutException -> mCallBack.onError(Throwable("Connect time out!", e))
                    is UnknownHostException,
                    is ConnectException -> mCallBack.onError(Throwable("Unable to connect to the network", e))
                    else -> mCallBack.onError(Throwable("Unable to connect to the service", e))
                }
                e.printStackTrace()
            }
        }

    }

    override fun onComplete() {

    }


}