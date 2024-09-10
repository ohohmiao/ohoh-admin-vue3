package com.ohohmiao.framework.mybatis.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 公共树形实体类
 *
 * @author ohohmiao
 * @date 2023-04-12 16:38
 */
@Getter
@Setter
public class CommonTreeEntity extends CommonEntity implements Serializable {

    /**
     * 父节点id
     */
    @TableField
    private String parentId;

    /**
     * 树层级
     */
    @TableField
    private Integer treeLevel;

    /**
     * 树路径
     */
    @TableField
    private String treePath;

    /**
     * 树排序
     */
    @TableField
    private Integer treeSort;

}
