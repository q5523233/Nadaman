package com.sm.nadaman.common.db;


import com.sm.nadaman.common.bean.Ecg12Data;
import com.techne.nomnompos.app.App;

import org.greenrobot.greendao.AbstractDao;

public class EcgDataOpe extends BaseOpeImp<Ecg12Data> {
    @Override
    protected AbstractDao createDao() {
        return App.Companion.getApp().getCurrentUserDaoSession().getEcg12DataDao();
    }
    public static EcgDataOpe create() {
        return new EcgDataOpe();
    }
}
