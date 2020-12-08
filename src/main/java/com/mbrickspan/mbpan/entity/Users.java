package com.mbrickspan.mbpan.entity;

public class Users {
    private Integer uid;

    private String uname;

    private String upwd;

    private String uemail;

    private String utel;

    public Users(Integer uid, String uname, String upwd, String uemail, String utel) {
        this.uid = uid;
        this.uname = uname;
        this.upwd = upwd;
        this.uemail = uemail;
        this.utel = utel;
    }

    public Users() {
        super();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd == null ? null : upwd.trim();
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail == null ? null : uemail.trim();
    }

    public String getUtel() {
        return utel;
    }

    public void setUtel(String utel) {
        this.utel = utel == null ? null : utel.trim();
    }
}