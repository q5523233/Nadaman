package com.sm.nadaman.common.bean;

import androidx.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Friend implements Serializable {
    static final long serialVersionUID = -1;
    @Id(autoincrement = true)
    public Long id;
    public String nickName;
    public String phone;
    public String flag;
    public @Nullable String imgPath;
    public long time;
    @Generated(hash = 1221105160)
    public Friend(Long id, String nickName, String phone, String flag,
            String imgPath, long time) {
        this.id = id;
        this.nickName = nickName;
        this.phone = phone;
        this.flag = flag;
        this.imgPath = imgPath;
        this.time = time;
    }
    @Generated(hash = 287143722)
    public Friend() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getFlag() {
        return this.flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getImgPath() {
        return this.imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
}
