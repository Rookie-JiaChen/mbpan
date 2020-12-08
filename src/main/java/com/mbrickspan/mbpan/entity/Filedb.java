package com.mbrickspan.mbpan.entity;

public class Filedb {
    private Integer fid;

    private String filename;

    private Integer ifdel;

    private String uname;

    private String fmd5;

    public Filedb(Integer fid, String filename, Integer ifdel, String uname, String fmd5) {
        this.fid = fid;
        this.filename = filename;
        this.ifdel = ifdel;
        this.uname = uname;
        this.fmd5 = fmd5;
    }

    public Filedb() {
        super();
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public Integer getIfdel() {
        return ifdel;
    }

    public void setIfdel(Integer ifdel) {
        this.ifdel = ifdel;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public String getFmd5() {
        return fmd5;
    }

    public void setFmd5(String fmd5) {
        this.fmd5 = fmd5 == null ? null : fmd5.trim();
    }
}