package net.edrop.edrop_employer.entity;
/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/28
 * Time: 8:40
 */
public class MsgItemBean {
    private int userId;
    private int employeeId;
    private String nickName;
    private String msg;
    private String date;
    private String employeeHeadImg;
    private String userHeadImg;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeHeadImg() {
        return employeeHeadImg;
    }

    public void setEmployeeHeadImg(String employeeHeadImg) {
        this.employeeHeadImg = employeeHeadImg;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

}
