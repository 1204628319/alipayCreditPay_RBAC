package com.greatwall.jhgx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.greatwall.component.ccyl.common.model.PageRequest;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.component.ccyl.common.service.ISuperService;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.domain.TSysRole;

import java.util.List;
import java.util.Set;

/**
 * 角色信息
 * @author zsd
 **/
public interface TSysRoleService extends ISuperService<TSysRole> {

    /**
     * get
     *
     * @param id
     * @return
     */
    TSysRole findById(long id);

    /**
     * create
     *
     * @param domain
     * @return
     */
    boolean create(TSysRole domain);

    /**
     * update
     *
     * @param domain
     * @return
     */
    boolean update(TSysRole domain);

    /**
     * delete
     *
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 更新角色权限信息
     *
     * @param domain
     */
    void updatePermission(TSysRole domain);

    /**
     * 更新角色菜单信息
     *
     * @param domain
     */
    void updateMenu(TSysRole domain);

    /**
     * 根据用户ID查询角色集合
     *
     * @param userId
     * @return
     */
    List<TSysRole> findRolesByUserId(Long userId);

    /**
     * 查询角色列表
     * @param operationId
     * @param name
     * @param pageRequest
     * @return
     */
    IPage<TSysRole> list(Long operationId, String name, PageRequest pageRequest);

    /**
     * 获取用户的子角色
     * @param userId
     * @param isIncludeSelf
     * @return
     */
    List<Long> getChildrenRoleIdListByUserId(Long userId, Boolean isIncludeSelf);

    /**
     * 创建
     * @param userDetails
     * @param roleDomain
     * @return
     */
    Result create(DefaultJwtUserDetails userDetails, TSysRole roleDomain);

    /**
     * 删除
     * @param userDetails
     * @param id
     * @param isForce
     * @return
     */
    Result delete(DefaultJwtUserDetails userDetails, Long id, Boolean isForce);

    /**
     * 更新
     * @param userDetails
     * @param roleDomain
     * @return
     */
    Result update(DefaultJwtUserDetails userDetails, TSysRole roleDomain);

    /**
     * 获取所有子角色
     * @param roleId
     * @param roleIdList
     */
    void getAllChildrenRoleIdListByRoleId(Long roleId, Set<Long> roleIdList);

    /**
     * 根据pid查询角色
     * @param pid
     * @return
     */
    List<TSysRole> findByPid(long pid);

    /**
     * 获取角色树
     * @param permissions
     * @return
     */
    Object getRoleTree(List<TSysRole> permissions);

    /**
     * 根据角色列表构建角色树
     * @param permissions
     * @return
     */
    Object getRoleTreeByList(List<TSysRole> permissions);
}
