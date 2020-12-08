package com.mbrickspan.mbpan.service.impl;

import org.springframework.stereotype.Service;
import com.mbrickspan.mbpan.dao.UsersMapper;
import com.mbrickspan.mbpan.entity.Users;
import com.mbrickspan.mbpan.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;

@Service("usersServiceImpl")
public class UsersServiceImpl  implements UsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users login(String uname,String upwd){

        return usersMapper.login(uname,upwd);
    }

    @Override
    public  int insertSelective(Users record){
        return usersMapper.insertSelective(record);
    }

    @Override
    public  Users selectByuname(String uname){
        return usersMapper.selectByuname(uname);
    }

    @Override
    public int updateByunameSelective(Users record){
        return usersMapper.updateByunameSelective(record);
    }

    @Override
    public int changingpwd(String uname,String upwd){
        return usersMapper.changingpwd(uname,upwd);
    }
}
