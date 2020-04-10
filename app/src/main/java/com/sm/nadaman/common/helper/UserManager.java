package com.sm.nadaman.common.helper;


import com.sm.nadaman.common.bean.User;
import com.sm.nadaman.common.utils.SpUtils;

public class UserManager {
    private User user;

    static class UserManagerSingle {
        static UserManager instance = new UserManager();
    }

    public static UserManager getInstance() {
        return UserManagerSingle.instance;
    }

    private UserManager() {
        if (user == null)
            user = SpUtils.getInstance().readObject("user", User.class);
    }

    public void setUser(User user) {
        this.user = user;
        SpUtils.getInstance().putObject("user", user);
    }

    public User getUser() {
        return user;
    }
}
