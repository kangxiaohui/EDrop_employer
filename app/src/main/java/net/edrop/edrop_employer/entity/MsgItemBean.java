package net.edrop.edrop_employer.entity;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/28
 * Time: 8:40
 */
public class MsgItemBean {
    private String nickName;
    private String msg;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
