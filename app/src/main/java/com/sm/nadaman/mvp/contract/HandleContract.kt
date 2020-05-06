package com.sm.nadaman.mvp.contract

import com.blackflagbin.kcommon.base.IBasePresenter
import com.blackflagbin.kcommon.base.IBaseView

interface HandleContract {
    interface IHandleModel {

    }

    interface IHandlePresenter : IBasePresenter {

    }

    interface IHandleView : IBaseView<Any?> {

    }
}
