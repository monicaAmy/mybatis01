package com.su.util;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 *     setParameter方法：
 *                  在生成SQL语句时被调用
 *     getResult：
 *               查询结束之后，在将ResultSet数据行装换为实体类对象时
 *               通知TypeHandler将当前数据行某个字段转换为何种类型
 * */
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class MyTypeHandler implements TypeHandler
{

    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException
    {
        System.out.println("parameter..start...");
        if (parameter == null)
        {// dept.flag=null   insertsql  flag设置0
            ps.setInt(i, 0);
            return;
        }
        String flag = (String) parameter;
        if ("男".equals(flag))
        {
            ps.setInt(i, 1);
        }
        else
        {
            ps.setInt(i, 0);
        }
    }

    public Object getResult(ResultSet rs, String columnName) throws SQLException
    {
        int flag = rs.getInt(columnName);
        System.out.println("result..start...");
        String myFlag = "女";
        if (flag == 1)
        {
            myFlag = "男";
        }
        return myFlag;
    }

    public Object getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        return null;
    }

    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        return null;
    }

}
