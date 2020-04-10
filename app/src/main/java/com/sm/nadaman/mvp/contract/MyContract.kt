package com.sm.nadaman.mvp.contract

import com.blackflagbin.kcommon.base.IBasePresenter
import com.blackflagbin.kcommon.base.IBaseView

interface MyContract {
    interface IMyModel {

    }

    interface IMyPresenter : IBasePresenter {

    }

    interface IMyView : IBaseView<Any?> {

    }
}
