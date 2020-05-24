package com.greatwall.jhgx.service;

import com.greatwall.component.ccyl.common.model.PageResult;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.component.ccyl.common.service.ISuperService;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.domain.TSysPermission;
import com.greatwall.jhgx.domain.TSysUser;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * 权限信息
 * @author xuxiangke
 **/
public interface TSysPermissionService extends ISuperService<TSysPermission> {
    /**
     * 生成权限集合
     *
     * @param user
     * @return
     */
    Collection<GrantedAuthority> mapToGrantedAuthorities(TSysUser user);

    TSysPermission findById(long id);

    /**
     * 根据角色ID获取权限集合
     * @param roleId
     * @return
     */
    List<TSysPermission> findByRoleId(long roleId);

    /**
     * create
     *
     * @param domain
     * @return
     */
    boolean create(TSysPermission domain);

    /**
     * update
     *
     * @param domain
     * @return
     */
    boolean update(TSysPermission domain);

    /**
     * 创建
     * @param userDetails
     * @param domain
     * @return
     */
    Result create(DefaultJwtUserDetails userDetails, TSysPermission domain);

    /**
     * 更新
     * @param userDetails
     * @param domain
     * @return
     */
    Result update(DefaultJwtUserDetails userDetails, TSysPermission domain);
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
     * @param permissions
     * @return
     */
    Object getPermissionTree(List<TSysPermission> permissions);

    /**
     * 获取菜单树
     * @param menuDomains
     * @return
     */
    Object getMenuTreeByList(List<TSysPermission> menuDomains);

    /**
     * findByPid
     *
     * @param pid
     * @return
     */
    List<TSysPermission> findByPid(long pid);

    /**
     * build Tree
     *
     * @param permissionDomains
     * @return
     */
    PageResult buildTree(List<TSysPermission> permissionDomains);

    /**
     * 根据条件查询权限
     * @param name
     * @return
     */
    List<TSysPermission> selectPermissionsByCondition(String name);
}
