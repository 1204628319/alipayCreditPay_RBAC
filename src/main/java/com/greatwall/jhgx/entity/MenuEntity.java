package com.greatwall.jhgx.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建前端路由时用到
 *
 * @author TianLei
 * @date 2019-06-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuEntity implements Serializable {
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单路径
     */
    private String path;
    /**
     * 重定向地址
     */
    private String redirect;
    /**
     * 组件信息
     */
    private String component;
    /**
     * 是否显示
     */
    private Boolean alwaysShow;
    /**
     * 元数据
     */
    private MenuMetaEntity meta;

    /**
     * 排序
     */
    private Integer sort;
    /**
     * 子菜单
     */
    private List<MenuEntity> children;
}
