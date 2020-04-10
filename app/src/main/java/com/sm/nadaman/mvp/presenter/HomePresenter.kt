package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.HomeContract
import com.sm.nadaman.mvp.model.HomeModel

class HomePresenter(iHomeView: HomeContract.IHomeView) :
    BasePresenter<HomeContract.IHomeModel, HomeContract.IHomeView>(iHomeView),
    HomeContract.IHomePresenter {

    override val model: HomeContract.IHomeModel
        get() = HomeModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
