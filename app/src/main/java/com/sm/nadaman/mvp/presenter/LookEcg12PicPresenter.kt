package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.LookEcg12PicContract
import com.sm.nadaman.mvp.model.LookEcg12PicModel

class LookEcg12PicPresenter(iLookEcg12PicView: LookEcg12PicContract.ILookEcg12PicView) :
    BasePresenter<LookEcg12PicContract.ILookEcg12PicModel, LookEcg12PicContract.ILookEcg12PicView>(
        iLookEcg12PicView
    ), LookEcg12PicContract.ILookEcg12PicPresenter {

    override val model: LookEcg12PicContract.ILookEcg12PicModel
        get() = LookEcg12PicModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
