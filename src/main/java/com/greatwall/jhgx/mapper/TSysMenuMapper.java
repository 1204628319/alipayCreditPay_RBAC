package com.greatwall.jhgx.mapper;

import com.greatwall.component.ccyl.db.mapper.SuperMapper;
import com.greatwall.jhgx.domain.TSysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 菜单信息Mapper
 *
 * @author xuxiangke
 * @date 2019/8/5
 */
public interface TSysMenuMapper extends SuperMapper<TSysMenu> {
    /**
     * 根据角色id查询
     * @param id
     * @return
     */
    List<TSysMenu> findByRoleId(Long id);

    /**
     * 根据菜单id删除角色菜单关联表
     * @param id
     */
    @Update("DELETE FROM T_SYS_ROLE_MENU WHERE MENU_ID = #{id}")
    void deleteMenuRoleRelations(@Param("id") Long id);

}
