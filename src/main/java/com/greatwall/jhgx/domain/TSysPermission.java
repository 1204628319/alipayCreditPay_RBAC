package com.greatwall.jhgx.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.greatwall.component.ccyl.common.model.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 权限信息表
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_SYS_PERMISSION")
public class TSysPermission extends SuperEntity {

    /**
     * 权限名称
     */
    @Length(max = 100, message = "名称太长")
    private String permissionName;

    /**
     * 别名
     */
    @Length(max = 100, message = "别名太长")
    private String alias;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 是否删除（1是0否）
     */
    @TableLogic
    private Boolean isDel = false;

    /**
     * 子权限
     */
    @TableField(exist = false)
    private List<TSysPermission> children;

    @TableField(exist = false)
    private String name;

    /**
     * 该节点是否是其它节点的子节点
     */
    @TableField(exist = false)
    private Boolean isChild = false;
}
