package com.mbrickspan.mbpan.dao;

import com.mbrickspan.mbpan.entity.Users;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer uid);

    int updateByunameSelective(Users record);

    int updateByPrimaryKey(Users record);

    Users login(@Param("uname")String uname, @Param("upwd")String upwd);

    Users selectByuname(String uname);

    int changingpwd(String uname,String upwd);
}