package com.sm.nadaman.common.event;

/**
 * Created by shenmai_vea on 2018/7/11.
 */

public class MsgEvent {
    private String action;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public MsgEvent(String action, String msg) {
        this.action = action;
        this.msg = msg;
    }

}
