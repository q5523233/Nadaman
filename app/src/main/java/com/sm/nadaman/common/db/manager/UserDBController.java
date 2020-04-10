package com.sm.nadaman.common.db.manager;



import com.sm.nadaman.common.bean.User;
import com.sm.nadaman.common.dao.DaoSession;
import com.sm.nadaman.common.utils.SpUtils;
import com.techne.nomnompos.app.App;

import java.util.List;

public class UserDBController {
    private static UserDBController instance = new UserDBController();
    private DaoSession mDaoSession;
    private User user;

    public static UserDBController getInstance() {
        return instance;
    }

    private UserDBController() {
    }

    public boolean createNewUser(User user, boolean isSwitch) {
        if (user == null) {
            return false;
        }
        List<User> users = UserListDB.getInstance().getUserList();
        if (users != null && users.contains(user)) {
            return false;
        }
        UserListDB.getInstance().insert(user);
        if (isSwitch) {
            switchUser(user);
        }
        return true;
    }

    public void switchUser(User user) {
        if (user == null) {
            return;
        }
        this.user = user;
        SpUtils.getInstance().putObject("user", user);
        UserInfoDaoMaster.OpenHelper helper = new UserInfoDaoMaster.OpenHelper(App.Companion.getApp(), user.getUserName() + "-db", null);
        UserInfoDaoMaster mDaoMaster = new UserInfoDaoMaster(helper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void initBaseDB() {
        UserListDB.getInstance();
    }

    public User getCurrentUser() {
        return user;
    }

    public List<User> getAllUsers() {
        return UserListDB.getInstance().getUserList();
    }
}
