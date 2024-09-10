package com.ohohmiao.framework.mybatis.config;

import com.ohohmiao.framework.mybatis.interceptor.MybatisLikeSqlInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 应用启动完成，mybatis plus拦截器注册逻辑
 *
 * @author ohohmiao
 * @date 2024-02-01 9:49
 */
@Slf4j
@Component
public class MyBatisPlusInterceptorRunner implements ApplicationRunner {

    /** Spring IOC容器始祖 **/
    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

    /**
     * 运行逻辑
     * @param args 系统启动时传入参数
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        SqlSessionFactory sqlSessionFactory =
                (SqlSessionFactory)defaultListableBeanFactory.getBean("sqlSessionFactory");
        // 查询特殊符号拦截
        sqlSessionFactory.getConfiguration().addInterceptor(new MybatisLikeSqlInterceptor());
    }

}
