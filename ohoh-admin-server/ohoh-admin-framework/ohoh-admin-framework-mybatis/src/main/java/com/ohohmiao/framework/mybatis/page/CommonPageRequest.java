package com.ohohmiao.framework.mybatis.page;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 通用分页请求
 *
 * @author ohohmiao
 * @date 2023-04-13 15:23
 */
@Slf4j
public class CommonPageRequest {

    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_PAGESIZE = 20;

    private static final Integer PAGE_SIZE_MAX_VALUE = 100;

    public static <T> Page<T> defaultPage() {
        return constructPage(null, null, null);
    }

    public static <T> Page<T> constructPage(Integer pageCurrent, Integer pageSize) {
        return constructPage(pageCurrent, pageSize, null);
    }

    public static <T> Page<T> constructPage(Integer pageCurrent, Integer pageSize, List<OrderItem> orderItemList) {
        int size = DEFAULT_PAGESIZE;
        int page = DEFAULT_PAGE;

        //每页条数
        if (ObjectUtil.isNotEmpty(pageSize)) {
            size = pageSize;
            if(size > PAGE_SIZE_MAX_VALUE) {
                size = PAGE_SIZE_MAX_VALUE;
            }
        }

        //第几页
        if (ObjectUtil.isNotEmpty(pageCurrent)) {
            page = pageCurrent;
        }
        Page<T> objectPage = new Page<>(page, size);
        if (ObjectUtil.isNotEmpty(orderItemList)) {
            objectPage.setOrders(orderItemList);
        }
        return objectPage;
    }

}
