package com.greatwall.jhgx.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.greatwall.component.ccyl.db.mapper.SuperMapper;
import com.greatwall.jhgx.domain.TSysRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 角色信息Mapper
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
public interface TSysRoleMapper extends SuperMapper<TSysRole> {
    /**
     * 查询用户的角色
     * @param userId
     * @return
     */
    @Select("SELECT T1.* FROM T_SYS_ROLE T1, T_SYS_USER_ROLE T2 WHERE T1.ID = T2.ROLE_ID AND T2.USER_ID = #{userId} ORDER BY ROLE_ID ASC")
    List<TSysRole> findRolesByUserId(@Param("userId") Long userId);

    /**
     * 分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("SELECT * FROM T_SYS_ROLE ${ew.customSqlSegment}")
    @Results({
            @Result(id = true, property = "id", column = "ID"),
            @Result(property = "permissions", column = "ID", many = @Many(select = "com.greatwall.jhgx.mapper.TSysPermissionMapper.findByRoleId")),
            @Result(property = "menus", column = "ID", many = @Many(select = "com.greatwall.jhgx.mapper.TSysMenuMapper.findByRoleId"))
    })
    List<TSysRole> selectByConditionAndPage(IPage<TSysRole> page, @Param(Constants.WRAPPER) Wrapper<TSysRole> queryWrapper);

}
