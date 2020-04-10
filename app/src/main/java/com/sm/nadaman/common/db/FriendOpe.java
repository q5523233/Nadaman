package com.sm.nadaman.common.db;


import com.sm.nadaman.common.bean.Friend;
import com.sm.nadaman.common.dao.FriendDao;
import com.techne.nomnompos.app.App;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by shenmai_vea on 2018/7/11.
 */

public class FriendOpe extends BaseOpeImp<Friend> {
    private FriendDao friendDao;

    @Override
    protected AbstractDao createDao() {
        friendDao = App.Companion.getApp().getCurrentUserDaoSession().getFriendDao();
        return friendDao;
    }

    public static FriendOpe create() {
        return new FriendOpe();
    }
}
