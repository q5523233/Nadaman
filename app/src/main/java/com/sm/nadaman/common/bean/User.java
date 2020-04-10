package com.sm.nadaman.common.bean;

/**
 * Created by shenmai_vea on 2018/7/11.
 */
public class User {
    private String userName;
    private boolean isSelect;

    public User() {
    }
    public User(String userName,boolean isSelect) {
        this.userName = userName;
        this.isSelect = isSelect;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        return ((User) obj).userName.equals(this.userName);
    }
}
