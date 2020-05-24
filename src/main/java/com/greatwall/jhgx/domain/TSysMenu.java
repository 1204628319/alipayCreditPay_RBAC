package com.greatwall.jhgx.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.greatwall.component.ccyl.common.model.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 菜单信息表
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_SYS_MENU")
public class TSysMenu extends SuperEntity {

    /**
     * 菜单名称
     */
    @Length(max = 20, message = "菜单名称太长")
    private String menuName;

    /**
     * 链接地址
     */
    @NotBlank(message = "链接地址不能为空")
    @Length(max = 255, message = "链接地址太长")
    private String menuPath;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 组件路径
     */
    @Length(max = 200, message = "组件路径太长")
    private String component;

    /**
     * 是否外链（1是0否）
     */
    private Boolean isFrame = false;

    /**
     * 是否删除（1是0否）
     */
    @TableLogic
    private Boolean isDel = false;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 排序
     */
    private Integer sort;

    @TableField(exist = false)
    private List<TSysMenu> children;

    @TableField(exist = false)
    private String name;
}
