package com.greatwall.jhgx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.greatwall.component.ccyl.common.consants.ResultCodeEnum;
import com.greatwall.component.ccyl.common.exception.BusinessException;
import com.greatwall.component.ccyl.common.exception.ParameterException;
import com.greatwall.component.ccyl.common.model.PageRequest;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.component.ccyl.common.service.impl.SuperServiceImpl;
import com.greatwall.component.ccyl.common.utils.PageQueryBuilder;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.cache.GuavaCacheService;
import com.greatwall.jhgx.constants.AdminConstants;
import com.greatwall.jhgx.domain.*;
import com.greatwall.jhgx.mapper.TSysRoleMapper;
import com.greatwall.jhgx.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色实现类
 *
 * @author TianLei
 */
@Service
public class TSysRoleServiceImpl extends SuperServiceImpl<TSysRoleMapper, TSysRole> implements TSysRoleService {

    @Autowired
    private TSysRoleMenuService tSysRoleMenuService;

    @Autowired
    private TSysRolePermissionService tSysRolePermissionService;

    @Autowired
    private TSysUserRoleService tSysUserRoleService;

    @Autowired
    private TSysUserService tSysUserService;

    /**
     * 本地缓存
     */
    @Autowired
    private GuavaCacheService guavaCacheService;

    /**
     * 菜单管理id
     **/
    private static final Long MENU_ID = 4L;

    /**
     * 权限管理id
     **/
    private static final Long PERMISSION_ID = 5L;

    private static final String ROLE_ID = "role_id";

    private static final String EXCEPTION_MSG_ROLE_NAME_EXIT = "已存在相同角色名";

    @Override
    public TSysRole findById(long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean create(TSysRole domain) {
        return retBool(baseMapper.insert(domain));
    }

    @Override
    public boolean update(TSysRole domain) {
        return retBool(baseMapper.updateById(domain));
    }

    @Override
    public boolean delete(Long id) {
        return retBool(baseMapper.deleteById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(TSysRole domain) {
        // 删除原有操作权限
        QueryWrapper<TSysRolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq(ROLE_ID, domain.getId());
        tSysRolePermissionService.remove(wrapper);

        // 新增现有操作权限
        List<TSysRolePermission> rolePermissions = Lists.newLinkedList();
        for (TSysPermission permissionDomain : domain.getPermissions()) {
            TSysRolePermission rolePermission = new TSysRolePermission();
            rolePermission.setPermissionId(permissionDomain.getId());
            rolePermission.setRoleId(domain.getId());
            rolePermissions.add(rolePermission);
        }
        tSysRolePermissionService.saveBatch(rolePermissions);

        // 刷新权限的缓存信息
        String key = "permission::findByRoleId:" + domain.getId();
        guavaCacheService.invalidate(key);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(TSysRole domain) {
        // 删除原有菜单关系
        QueryWrapper<TSysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.eq(ROLE_ID, domain.getId());
        tSysRoleMenuService.remove(wrapper);
        TSysRole roleDomain = this.getById(domain.getId());
        // 添加新的菜单关系
        List<TSysRoleMenu> roleMenuDomains = Lists.newLinkedList();
        for (TSysMenu menuDomain : domain.getMenus()) {
            TSysRoleMenu roleMenu = new TSysRoleMenu();
            Long menuId = menuDomain.getId();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(domain.getId());
            roleMenuDomains.add(roleMenu);
            boolean  isLegal=MENU_ID.equals(menuId) || PERMISSION_ID.equals(menuId);
            if( ! roleDomain.getIsSystem() && isLegal){
                throw new BusinessException("非系统管理员不能添加菜单权限和数据权限");
            }
        }
        tSysRoleMenuService.saveBatch(roleMenuDomains);
    }

    @Override
    public List<TSysRole> findRolesByUserId(Long userId) {
        return baseMapper.findRolesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IPage<TSysRole> list(Long operationId, String name, PageRequest pageRequest){
        Map<Long, String> roleNameMap = Maps.newHashMap();
        IPage<TSysRole> rolePage = new PageQueryBuilder<TSysRole>().getPage(pageRequest);
        QueryWrapper<TSysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "ROLE_NAME", name);
        queryWrapper.eq("is_del", false);
        if( ! operationId.equals(AdminConstants.ADMIN_USER_ID)){
            // 只能获取子角色
            List<Long> childrenIdList = this.getChildrenRoleIdListByUserId(operationId, false);
            if(CollectionUtils.isEmpty(childrenIdList)){
                return rolePage.setRecords(new ArrayList<>());
            }
            queryWrapper.in("id", childrenIdList);
        }
        List<TSysRole> records = baseMapper.selectByConditionAndPage(rolePage, queryWrapper);

        for(TSysRole roleDomain : records) {
            roleDomain.setParentRoleName(this.getNameById(roleDomain.getPid(), roleNameMap));
        }
        return rolePage.setRecords(records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> getChildrenRoleIdListByUserId(Long userId, Boolean isIncludeSelf){
        Set<Long> roleIdList = new HashSet<>();
        TSysUser tSysUser = tSysUserService.findById(userId);
        tSysUser.getRoles().forEach(tSysRole -> {
            if(isIncludeSelf){
                roleIdList.add(tSysRole.getId());
            }
            this.getAllChildrenRoleIdListByRoleId(tSysRole.getId(), roleIdList);
        });
        return new ArrayList<>(roleIdList);
    }

    private String getNameById(Long id, Map<Long, String> map){
        if(map.containsKey(id)){
            return map.get(id);
        }
        if(AdminConstants.DEFAULT_PARENT_ID.equals(id)){
            return AdminConstants.ADMIN_NAME;
        }
        TSysRole tSysRole = this.getById(id);
        String roleName = tSysRole.getRoleName();
        map.put(id, roleName);
        return roleName;
    }

    public List<TSysRole> findByRoleName(String roleName) {
        QueryWrapper<TSysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", roleName);
        return baseMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result create(DefaultJwtUserDetails userDetails, TSysRole roleDomain){
        roleDomain.setId(null);
        roleDomain.setCreateBy(userDetails.getId());
        roleDomain.setUpdateBy(userDetails.getId());
        String roleName = roleDomain.getRoleName();
        if(roleName.contains(StringUtils.SPACE)){
            throw new ParameterException("角色名称中含有空格");
        }
        List<TSysRole> roleDomainList = this.findByRoleName(roleName);
        if(CollectionUtils.isNotEmpty(roleDomainList)){
            throw new ParameterException(EXCEPTION_MSG_ROLE_NAME_EXIT);
        }
        if (this.create(roleDomain)) {
            if(isMoreThanFive(roleDomain.getId())){
                throw new BusinessException("角色树不能超过五层");
            }
            return Result.succeed("新增角色成功");
        } else {
            return Result.failed("新增角色失败");
        }
    }

    private Boolean isMoreThanFive(Long id){
        Long selectId = id;
        for(int i = 0; i < AdminConstants.TREE_MAX_DEEP+1; i++){
            TSysRole tSysRole = this.findById(selectId);
            if(null != tSysRole){
                selectId = tSysRole.getPid();
            }else{
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(DefaultJwtUserDetails userDetails, TSysRole roleDomain){
        TSysRole existRole = this.getById(roleDomain.getId());
        existRole.setUpdateBy(userDetails.getId());
        Long roleId = roleDomain.getId();
        String roleName = roleDomain.getRoleName();
        if(roleName.contains(StringUtils.SPACE)){
            throw new ParameterException("角色名称中含有空格");
        }
        List<TSysRole> roleDomainList = this.findByRoleName(roleName);
        if(CollectionUtils.isNotEmpty(roleDomainList)){
            if(roleDomainList.size() > 1){
                throw new ParameterException(EXCEPTION_MSG_ROLE_NAME_EXIT);
            }else{
                TSysRole roleDomainExist = roleDomainList.get(0);
                if(roleDomainExist.getId() != roleId.longValue()){
                    throw new ParameterException(EXCEPTION_MSG_ROLE_NAME_EXIT);
                }
            }
        }
        if(roleDomain.getPid().equals(roleDomain.getId())){
            throw new BusinessException("父级角色不能为自己");
        }
        existRole.setRoleName(roleDomain.getRoleName());
        existRole.setRoleDesc(roleDomain.getRoleDesc());
        existRole.setUpdateAt(new Date());
        existRole.setPid(roleDomain.getPid());
        if (this.update(existRole)) {
            if(isMoreThanFive(roleDomain.getId())){
                throw new BusinessException("角色树不能超过五层");
            }
            return Result.succeed("修改角色成功");
        } else {
            return Result.failed("修改角色失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(DefaultJwtUserDetails userDetails, Long id, Boolean isForce){
        TSysRole roleDomain = this.getById(id);
        if(null == roleDomain){
            throw new ParameterException("未找到相应的角色");
        }
        if(roleDomain.getIsSystem()){
            throw new ParameterException("系统角色不能删除");
        }
        QueryWrapper<TSysRole> tSysRoleQueryWrapper = new QueryWrapper<>();
        tSysRoleQueryWrapper.eq("pid", id);
        List<TSysRole> tSysRoleList = this.list(tSysRoleQueryWrapper);
        if(CollectionUtils.isNotEmpty(tSysRoleList)){
            throw new ParameterException("请先删除该角色的子角色： " + tSysRoleList.get(0).getRoleName());
        }
        Map<String, Object> map = Maps.newHashMap();
        // 查看是否已有用户绑定该角色
        QueryWrapper<TSysUserRole> userRoleDomainQueryWrapper = new QueryWrapper<>();
        userRoleDomainQueryWrapper.eq(ROLE_ID, id);
        if( ! isForce){
            List<TSysUserRole> userRoleDomainList = tSysUserRoleService.list(userRoleDomainQueryWrapper);
            if(CollectionUtils.isNotEmpty(userRoleDomainList)){
                map.put("code", ResultCodeEnum.ERROR.getCode());
                return Result.succeed(map);
            }
        }
        if (this.delete(id)) {
            tSysUserRoleService.remove(userRoleDomainQueryWrapper);
            map.put("code", ResultCodeEnum.SUCCESS.getCode());
            return Result.succeed(map);
        }
        return Result.failed("删除角色失败");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getAllChildrenRoleIdListByRoleId(Long roleId, Set<Long> roleIdList){
        QueryWrapper<TSysRole> tSysRoleQueryWrapper = new QueryWrapper<>();
        tSysRoleQueryWrapper.eq("pid", roleId);
        List<TSysRole> tSysRoleList = this.list(tSysRoleQueryWrapper);
        tSysRoleList.forEach(tSysRole -> {
            Long itemRoleId = tSysRole.getId();
            roleIdList.add(itemRoleId);
            getAllChildrenRoleIdListByRoleId(itemRoleId, roleIdList);
        });
    }

    @Override
    public List<TSysRole> findByPid(long pid) {
        QueryWrapper<TSysRole> wrapper = new QueryWrapper<>();
        wrapper.eq(AdminConstants.ADMIN_PID_KEY, pid);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Object getRoleTree(List<TSysRole> permissions){
        // 定义权限树存储对象
        List<Map<String, Object>> list = Lists.newLinkedList();
        permissions.forEach(organizationDomain -> {
                if (Objects.nonNull(organizationDomain)) {
                    List<TSysRole> tSysRoleList = findByPid(organizationDomain.getId());
                    Map<String, Object> map = Maps.newHashMap();
                    map.put(AdminConstants.ADMIN_ID_KEY, organizationDomain.getId());
                    map.put(AdminConstants.ADMIN_LABEL_KEY, organizationDomain.getRoleName());
                    if (CollectionUtils.isNotEmpty(tSysRoleList)) {
                        map.put(AdminConstants.ADMIN_CHILDREN_KEY, getRoleTree(tSysRoleList));
                    }
                    list.add(map);
                }
            }
        );
        return list;
    }


    @Override
    public Object getRoleTreeByList(List<TSysRole> tSysRoles) {
        List<TSysRole> tSysRoleList = buildListToTree(tSysRoles);
        return buildTreeByList(tSysRoleList);
    }

    private Object buildTreeByList(List<TSysRole> tSysRoleList){
        List<Map<String, Object>> list = Lists.newLinkedList();
        tSysRoleList.forEach(menuDomain -> {
                if (Objects.nonNull(menuDomain)) {
                    Map<String, Object> retMap = Maps.newHashMap();
                    retMap.put(AdminConstants.ADMIN_ID_KEY, menuDomain.getId());
                    retMap.put(AdminConstants.ADMIN_LABEL_KEY, menuDomain.getRoleName());
                    if (CollectionUtils.isNotEmpty(menuDomain.getChildren())) {
                        retMap.put(AdminConstants.ADMIN_CHILDREN_KEY, buildTreeByList(menuDomain.getChildren()));
                    }
                    list.add(retMap);
                }
            }
        );
        return list;
    }

    private List<TSysRole> buildListToTree(List<TSysRole> dirs) {
        List<TSysRole> roots = findRoots(dirs);
        List<TSysRole> notRoots = (List<TSysRole>) CollectionUtils
                .subtract(dirs, roots);
        for (TSysRole root : roots) {
            root.setChildren(findChildren(root, notRoots));
        }
        return roots;
    }

    private List<TSysRole> findRoots(List<TSysRole> allNodes) {
        List<TSysRole> results = new ArrayList<>();
        for (TSysRole node : allNodes) {
            boolean isRoot = true;
            for (TSysRole comparedOne : allNodes) {
                if (node.getPid().equals(comparedOne.getId())) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                results.add(node);
            }
        }
        return results;
    }

    private List<TSysRole> findChildren(TSysRole root, List<TSysRole> allNodes) {
        List<TSysRole> children = new ArrayList<>();
        for (TSysRole comparedOne : allNodes) {
            if (comparedOne.getPid().equals(root.getId())) {
                children.add(comparedOne);
            }
        }
        List<TSysRole> notChildren = (List<TSysRole>) CollectionUtils.subtract(allNodes, children);
        for (TSysRole child : children) {
            List<TSysRole> tmpChildren = findChildren(child, notChildren);
            child.setChildren(tmpChildren);
        }
        return children;
    }
}
