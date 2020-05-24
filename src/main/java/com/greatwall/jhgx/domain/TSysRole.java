package com.greatwall.jhgx.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.greatwall.component.ccyl.common.model.SuperEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 角色信息表
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_SYS_ROLE")
public class TSysRole extends SuperEntity {

    /**
     * 角色名称
     */
    @Length(max = 20, message = "角色名称太长")
    private String roleName;

    /**
     * 描述
     */
    private String roleDesc;

    /**
     * 上级角色
     */
    private Long pid;

    /**
     * 是否系统角色
     */
    @ApiModelProperty(hidden = true)
    private Boolean isSystem = false;

    /**
     * 角色英文别名
     */
    private String alias;

    /**
     * 是否删除 1是 0否
     */
    @TableLogic
    private Boolean isDel = false;

    /**
     * 角色权限集合
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<TSysPermission> permissions;

    /**
     * 角色菜单集合
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<TSysMenu> menus;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String parentRoleName;

    @TableField(exist = false)
    private List<TSysRole> children;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private Boolean isForce;

    @TableField(exist = false)
    private Boolean isCreateUser;
}
