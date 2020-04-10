package com.sm.nadaman.common.db.manager;

import com.sm.nadaman.common.bean.User;
import com.sm.nadaman.common.bean.UserList;
import com.sm.nadaman.common.dao.UserListDao;
import com.techne.nomnompos.app.App;

import java.util.ArrayList;
import java.util.List;

public class UserListDB {
    private static UserListDB ope;
    private UserListDao userListDao;

    private UserListDB() {
        UserListDaoMaster.OpenHelper helper = new UserListDaoMaster.OpenHelper(App.Companion.getApp(), "user-db", null);
        userListDao = new UserListDaoMaster(helper.getWritableDatabase()).newSession().getUserListDao();
    }

    public static UserListDB getInstance() {
        if (ope == null) {
            ope = new UserListDB();
        }
        return ope;
    }

    public List<User> getUserList() {
        UserList result = userListDao.queryBuilder().unique();
        return result == null ? null : result.getUsers();
    }


    public boolean insert(User f) {
        UserList entity = userListDao.queryBuilder().unique();
        if (entity == null) entity = new UserList();
        List<User> userList = entity.getUsers();
        if (userList == null) {
            userList = new ArrayList<>();
            entity.setUsers(userList);
            userList.add(f);
        } else if (!userList.contains(f)) {
            userList.add(f);
        }
        return userListDao.insertOrReplace(entity) > 0;
    }


}
