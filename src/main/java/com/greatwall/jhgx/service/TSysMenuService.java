package com.greatwall.jhgx.service;

import com.greatwall.component.ccyl.common.model.PageResult;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.component.ccyl.common.service.ISuperService;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.domain.TSysMenu;
import com.greatwall.jhgx.domain.TSysRole;
import com.greatwall.jhgx.entity.MenuEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Set;

/**
 * 菜单信息
 * @author xuxiangke
 **/
public interface TSysMenuService extends ISuperService<TSysMenu> {

    /**
     * get
     *
     * @param id
     * @return
     */
    TSysMenu findById(long id);

    /**
     * create
     *
     * @param domain
     * @return
     */
    boolean create(TSysMenu domain);

    /**
     * 创建
     * @param userDetails
     * @param domain
     * @return
     */
    Result create(DefaultJwtUserDetails userDetails, TSysMenu domain);

    /**
     * update
     *
     * @param domain
     * @return
     */
    boolean update(TSysMenu domain);

    /**
     * 更新
     * @param userDetails
     * @param domain
     * @return
     */
    Result update(DefaultJwtUserDetails userDetails, TSysMenu domain);

    /**
     * delete
     *
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * permission tree
     *
     * @param menus
     * @return
     */
    Object getMenuTree(List<TSysMenu> menus);

    /**
     * 获取菜单树
     * @param menuDomains
     * @return
     */
    Object getMenuTreeByList(List<TSysMenu> menuDomains);

    /**
     * findByPid
     *
     * @param pid
     * @return
     */
    List<TSysMenu> findByPid(long pid);

    /**
     * build Tree
     *
     * @param menuDomains
     * @return
     */
    PageResult buildTree(List<TSysMenu> menuDomains);

    /**
     * findByRoles
     *
     * @param roles
     * @return
     */
    List<TSysMenu> findByRoles(List<TSysRole> roles);

    /**
     * buildMenus
     *
     * @param roleMenus
     * @return
     */
    List<MenuEntity> buildMenus(Set<TSysMenu> roleMenus);

    /**
     * 根据条件查询菜单
     * @param name
     * @return
     */
    List<TSysMenu> selectMenusByCondition(String name);
}
