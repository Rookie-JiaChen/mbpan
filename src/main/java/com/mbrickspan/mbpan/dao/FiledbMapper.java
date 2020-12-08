package com.mbrickspan.mbpan.dao;

import com.mbrickspan.mbpan.entity.Filedb;

import java.io.File;
import java.util.List;

public interface FiledbMapper {
    int deleteByPrimaryKey(Integer fid);

    int insert(Filedb record);

    int insertSelective(Filedb record);

    Filedb selectByPrimaryKey(Integer fid);

    int updateByPrimaryKeySelective(Filedb record);

    int updateByPrimaryKey(Filedb record);
    //update方法 更新ifdel的值
    int del(Integer fid);
    int recover(Integer fid);
    int shiftdel(String filename);

    List<Filedb> selectfilebyuname(String uname);

    List<Filedb> selectbinbyuname(String uname);

    Filedb selectfilebyfmd5(String fmd5,String uname);

    List<Filedb> selectfilefuzzily(String filename);

}