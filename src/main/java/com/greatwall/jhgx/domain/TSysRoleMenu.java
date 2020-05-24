package com.greatwall.jhgx.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色菜单表
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_SYS_ROLE_MENU")
public class TSysRoleMenu implements Serializable {
    /**
     * 主键ID，采用雪花算法生成
     */
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
