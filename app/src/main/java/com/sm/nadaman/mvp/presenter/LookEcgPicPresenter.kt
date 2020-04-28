package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.LookEcgPicContract
import com.sm.nadaman.mvp.model.LookEcgPicModel

class LookEcgPicPresenter(iLookEcgPicView: LookEcgPicContract.ILookEcgPicView) :
    BasePresenter<LookEcgPicContract.ILookEcgPicModel, LookEcgPicContract.ILookEcgPicView>(
        iLookEcgPicView
    ), LookEcgPicContract.ILookEcgPicPresenter {

    override val model: LookEcgPicContract.ILookEcgPicModel
        get() = LookEcgPicModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
