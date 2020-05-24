package com.greatwall.jhgx.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户角色表
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_SYS_USER_ROLE")
public class TSysUserRole implements Serializable {
    /**
     * 主键ID，采用雪花算法生成
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;
}
