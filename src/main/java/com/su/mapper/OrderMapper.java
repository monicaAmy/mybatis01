package com.su.mapper;

import com.su.pojo.Order;

import java.util.List;

public interface OrderMapper
{
    List<Order> queryOrderAll();

    List<Order> queryOrderUser();

    /**
     * 返回最近插入的id
     *
     * @return
     */
    int querylastInsertId();
}
