package com.mbrickspan.mbpan.service.impl;

import com.mbrickspan.mbpan.dao.FiledbMapper;
import com.mbrickspan.mbpan.entity.Filedb;
import com.mbrickspan.mbpan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {

    @Autowired
    private FiledbMapper filedbMapper;

    @Override
    public int insertSelective(Filedb record){
        return filedbMapper.insertSelective(record);
    }

    @Override
    public List<Filedb> selectfilebyuname(String uname){
        return filedbMapper.selectfilebyuname(uname);
    }

    @Override
    public  Filedb selectfilebyfmd5(String fmd5,String uname){
        return  filedbMapper.selectfilebyfmd5(fmd5,uname);
    }

    @Override
    public  List<Filedb> selectbinbyuname(String uname){
        return filedbMapper.selectbinbyuname(uname);
    }

    @Override
    public  int del(Integer fid){
        return filedbMapper.del(fid);
    }

    @Override
    public   int recover(Integer fid){
        return filedbMapper.recover(fid);
    }
    @Override
    public int shiftdel(String filename){
        return filedbMapper.shiftdel(filename);
    }

    @Override
    public List<Filedb> selectfilefuzzily(String filename){
        return filedbMapper.selectfilefuzzily(filename);
    }


}
