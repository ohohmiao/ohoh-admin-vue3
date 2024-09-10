package com.ohohmiao;

import com.ohohmiao.framework.common.listener.CommonDataChangeEventCenter;
import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;

/**
 * SpringBoot启动类
 *
 * @author ohohmiao
 * @date 2023-03-30 11:50
 */
@SpringBootApplication
public class Application {

    /**
     * 注册自定义的数据变更监听器
     * @param dataChangeListenerList
     */
    @Resource
    public void registerCommonDataChangeListenerList(List<CommonDataChangeListener> dataChangeListenerList) {
        CommonDataChangeEventCenter.registerListenerList(dataChangeListenerList);
    }

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

}
