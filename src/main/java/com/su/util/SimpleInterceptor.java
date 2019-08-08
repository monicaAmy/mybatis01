package com.su.util;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * MyBatis自定义拦截器，可以拦截接口只有四种.
 * Executor.class
 * StatementHandler.class  子类PreparedStatementHandler用于生成PreparedStatement ,预编译sql
 * ParameterHandler.class  负责传参到PreparedStatement里面,决定值交给哪个属性
 * ResultSetHandler.class
 */
@Intercepts(
        {
                @Signature(
                        method = "query", //来源于Executor里面的方法名
                        type = Executor.class,
                        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class} //query里面的参数
                )

        }
)
public class SimpleInterceptor implements Interceptor
{
    /*
     *
     * 参数：Invocation{ 代理对象 ，被监控方法对象  ，当前被监控方法运行时需要实参}
     *
     **/
    public Object intercept(Invocation invocation) throws Throwable
    {
        System.out.println("被拦截方法执行之前，做的辅助服务.....");
        Object object = invocation.proceed();//执行被拦截方法
        System.out.println("被拦截方法执行之后，做的辅助服务.....");
        return object;
    }

    /*
     *
     *  参数： target 表示被拦截对象,应该Executor接口实例对象
     *  作用：
     *        如果被拦截对象所在的类有实现接口
     *        就为当前拦截对象生成一个【$Proxy】
     *
     *        如果被拦截对象所在的类没有指定接口
     *        这个对象之后行为就不会被代理操作
     *
     * */
    public Object plugin(Object target)
    {

        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties)
    {

    }

}
