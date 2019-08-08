package com.su.util;

import com.su.pojo.User;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

/**
 * mybatis创建结果集里面的对象时使用
 * DefaultObjectFactory 只能调用实体类默认构造方法进行处理
 * 创建对象之后,根据数据库的返回值进行赋值
 */
public class MyObjectFactory extends DefaultObjectFactory
{

    @Override
    public Object create(Class type)
    {

        System.out.println("MyObjectFactory start....");
        //重新定义User类实例对象创建规则，其他类实例对象创建规则不想改变
        if (User.class == type)
        {
            //依靠父类提供create方法创建Dept实例对象
            User user = (User) super.create(type);
            //会被数据库里面的值覆盖
            // user.setAddress("ttttt");
            user.setNich("6666");
            return user;
        }
        return super.create(type);
    }

}
