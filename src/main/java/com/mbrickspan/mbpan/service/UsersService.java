package com.mbrickspan.mbpan.service;

import com.mbrickspan.mbpan.entity.Users;

public interface UsersService {
    Users login(String uname,String upwd);
    int insertSelective(Users record);
    Users selectByuname(String uname);
    int updateByunameSelective(Users record);
    int changingpwd(String uname,String upwd);
}
