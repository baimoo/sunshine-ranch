package com.baimo.pojo;

import java.util.List;

/**
 * 用户实体类
 */
public class User {
    private String account;//账号
    private String passWord;//密码
    private String sid;//每次登录即改变
    private String uid;//家园id
    private String openId;//农场key，每个账号唯一
    private List<String> fieldId;//每个菜坑的id，每号每坑唯一且固定

    public User() {
    }
    public User(String account, String passWord) {//在new对象时赋予账号、密码。
        this.account = account;
        this.passWord = passWord;
    }

    public List<String> getFieldId() {
        return fieldId;
    }

    public void setFieldId(List<String> fieldId) {
        this.fieldId = fieldId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", passWord='" + passWord + '\'' +
                ", sid='" + sid + '\'' +
                ", uid='" + uid + '\'' +
                ", openId='" + openId + '\'' +
                '}';
    }
}
