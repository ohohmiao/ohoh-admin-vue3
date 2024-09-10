package com.ohohmiao.framework.mybatis.interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.*;

/**
 * Mybatis Plus查询特殊符号拦截逻辑
 *
 * @author ohohmiao
 * @date 2024-02-01 9:25
 */
@Slf4j
@Intercepts({@Signature(
        type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MybatisLikeSqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement)args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        transferLikeSql(sql, parameterObject, boundSql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target){
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties){}

    /**
     * 处理查询sql
     * @param sql SQL语句
     * @param parameterObject 参数对象
     * @param boundSql 绑定SQL
     */
    private void transferLikeSql(String sql, Object parameterObject, BoundSql boundSql){
        if(!(parameterObject instanceof HashMap)){
            return;
        }
        if(!isEscape(sql)){
            return;
        }
        Set<String> keyNames = listKeyNames(sql, boundSql);
        // 对关键字进行特殊字符“清洗”，如果有特殊字符的，在特殊字符前添加转义字符（\）
        for(String keyName: keyNames){
            HashMap parameter = (HashMap) parameterObject;
            if(keyName.contains("ew.paramNameValuePairs.") && sql.toLowerCase().contains(" like ?")){
                // 第一种情况：在业务层进行条件构造产生的模糊查询关键字
                AbstractWrapper wrapper = (AbstractWrapper) parameter.get("ew");
                parameter = (HashMap) wrapper.getParamNameValuePairs();
                String[] keyList = keyName.split("\\.");
                // ew.paramNameValuePairs.MPGENVAL1，截取字符串之后，获取第三个，即为参数名
                Object a = parameter.get(keyList[2]);
                if(a instanceof String && hasEscapeChar(a.toString())){
                    parameter.put(keyList[2],
                            "%" + escapeChar(a.toString().substring(1, a.toString().length() - 1)) + "%");
                }
            }else if(!keyName.contains("ew.paramNameValuePairs.") && sql.toLowerCase().contains(" like ?")){
                // 第二种情况：未使用条件构造器，但是在service层进行了查询关键字与模糊查询符`%`手动拼接
                Object a = parameter.get(keyName);
                if(a instanceof String && hasEscapeChar(a.toString())){
                    parameter.put(keyName,
                            "%" + escapeChar(a.toString().substring(1, a.toString().length() - 1)) + "%");
                }
            }else{
                // 第三种情况：在Mapper类的注解SQL中进行了模糊查询的拼接
                Object a = parameter.get(keyName);
                if(a instanceof String && hasEscapeChar(a.toString())){
                    parameter.put(keyName, escapeChar(a.toString()));
                }
            }
        }
    }

    /**
     * 处理转义字符串
     * @param str 字符串
     * @return 转义结果
     */
    private String escapeChar(String str){
        if(StrUtil.isNotBlank(str)){
            str = str.replaceAll("\\\\", "\\\\\\\\");
            str = str.replaceAll("_", "\\\\_");
            str = str.replaceAll("%", "\\\\%");
        }
        return str;
    }

    /**
     * 判断是否存在需转义的字符
     * @param str 字符串
     * @return 结果
     */
    private boolean hasEscapeChar(String str){
        if(StrUtil.isBlank(str)){
            return false;
        }
        boolean isExist = false;
        String[] escapeChars = { "%", "_", "\\" };
        for(String s: escapeChars){
            if(str.contains(s)){
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * 是否需转义
     * @param sql SQL语句
     * @return 结果
     */
    private boolean isEscape(String sql){
        return (hasLike(sql)) && (hasPlaceholder(sql));
    }

    /**
     * 判断sql语句中是否存在like
     * @param sql SQL语句
     * @return 结果
     */
    private boolean hasLike(String sql){
        if(StrUtil.isBlank(sql)){
            return false;
        }
        return sql.toLowerCase().contains(" like ");
    }

    /**
     * 判断sql语句中是否存在查询占位符
     * @param sql SQL语句
     * @return 结果
     */
    private boolean hasPlaceholder(String sql){
        if(StrUtil.isBlank(sql)){
            return false;
        }
        return sql.toLowerCase().contains("?");
    }

    /**
     * 列出查询属性名称
     * @param sql SQL语句
     * @param boundSql 绑定SQL
     * @return 查询属性名称集合
     */
    private Set<String> listKeyNames(String sql, BoundSql boundSql){
        String[] params = sql.split("\\?");
        Set<String> fields = new HashSet<>();
        for(int i = 0; i < params.length; i++){
            if(hasLike(params[i])){
                String field = boundSql.getParameterMappings().get(i).getProperty();
                fields.add(field);
            }
        }
        return fields;
    }

}
