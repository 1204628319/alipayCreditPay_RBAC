package com.greatwall.jhgx.mapper;

import com.greatwall.component.ccyl.db.mapper.SuperMapper;
import com.greatwall.jhgx.domain.TSysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限信息Mapper
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
public interface TSysPermissionMapper extends SuperMapper<TSysPermission> {
    /**
     * 查询角色的权限
     * @param roleId
     * @return
     */
    @Select("SELECT T1.* FROM T_SYS_PERMISSION T1, T_SYS_ROLE_PERMISSION T2 WHERE T1.ID = T2.PERMISSION_ID AND T2.ROLE_ID = #{roleId}")
    List<TSysPermission> findByRoleId(@Param("roleId") Long roleId);

}
