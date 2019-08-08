package com.su.mapper;

import com.su.pojo.Dept;

import java.util.List;

public interface DeptMapper
{
    public void deptSave(Dept dept);

    public List<Dept> deptFind();
}



