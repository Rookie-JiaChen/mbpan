package com.mbrickspan.mbpan.service;

import com.mbrickspan.mbpan.entity.Filedb;

import java.util.List;

public interface FileService {

    int insertSelective(Filedb record);

    List<Filedb> selectfilebyuname(String uname);

    Filedb selectfilebyfmd5(String fmd5,String uname);

    List<Filedb> selectbinbyuname(String uname);

    List<Filedb> selectfilefuzzily(String filename);

    int del(Integer fid);
    int recover(Integer fid);

    int shiftdel(String filename);


}
