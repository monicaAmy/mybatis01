package com.su.mapper;

import com.su.pojo.QueryVo;
import com.su.pojo.User;

import java.util.List;

public interface UserMapper
{
    User queryUserById(int id);

    List<User> queryUserByQueryVo(QueryVo queryVo);

    List<User> queryUser(User user);

    int queryCount();
}
