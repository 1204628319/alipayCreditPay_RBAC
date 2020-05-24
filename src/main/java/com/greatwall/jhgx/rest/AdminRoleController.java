package com.greatwall.jhgx.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Maps;
import com.greatwall.component.ccyl.common.model.PageRequest;
import com.greatwall.component.ccyl.common.model.PageResult;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.componnet.auth.annotation.LoginUser;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.constants.AdminConstants;
import com.greatwall.jhgx.domain.TSysRole;
import com.greatwall.jhgx.domain.TSysUser;
import com.greatwall.jhgx.service.TSysRoleService;
import com.greatwall.jhgx.service.TSysUserService;
import com.greatwall.jhgx.util.multibody.MultiRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 门户管理后台角色信息管理
 *
 * @author TianLei
 * @date 2019-06-20
 */
@RestController
@RequestMapping("admin/roles")
@Api(tags = "角色")
public class AdminRoleController {

    @Autowired
    private TSysRoleService tSysRoleService;

    @Autowired
    private TSysUserService tSysUserService;

    @PostMapping(value = "getMap")
    public Result getMap(@LoginUser DefaultJwtUserDetails userDetails) {
        List<Long> haveRoleIdList = tSysRoleService.getChildrenRoleIdListByUserId(userDetails.getId(), true);
        List<TSysRole> roleList = tSysRoleService.list();
        Map<Long, String> map = Maps.newHashMap();
        for (TSysRole roleDomain : roleList) {
            Long roleId = roleDomain.getId();
            if(haveRoleIdList.contains(roleId)){
                map.put(roleId, roleDomain.getRoleName());
            }
        }
        return Result.succeed(map);
    }

    @PostMapping(value = "getById")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public Result getRoles(@RequestBody TSysRole tSysRole) {
        return Result.succeed(tSysRoleService.getById(tSysRole.getId()));
    }

    @ApiOperation("查询父级树")
    @PostMapping(value = "getAllParent")
    public Result getAllParent(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody TSysRole paramsTSysRole) {
        Long operationId = userDetails.getId();
        Long id = paramsTSysRole.getId();
        if(AdminConstants.ADMIN_USER_ID.equals(operationId)) {
            List<Map<String, Long>> tSysRoleList = new ArrayList<>();
            for (int i = 0; i < AdminConstants.TREE_MAX_DEEP + 1; i++) {
                TSysRole tSysRole = tSysRoleService.findById(id);
                if (null != tSysRole) {
                    Map<String, Long> map = Maps.newHashMap();
                    map.put("id", id);
                    id = tSysRole.getPid();
                    map.put("pid", id);
                    tSysRoleList.add(map);
                } else {
                    break;
                }
            }
            return Result.succeed(tSysRoleList);
        }else{
            TSysUser tSysUser = tSysUserService.findById(userDetails.getId());
            final Set<Long> roleIdList = new HashSet<>();
            tSysUser.getRoles().forEach(tSysRole ->{
                roleIdList.add(tSysRole.getId());
                tSysRoleService.getAllChildrenRoleIdListByRoleId(tSysRole.getId(), roleIdList);
            });
            List<Map<String, Long>> tSysRoleList = new ArrayList<>();
            for (int i = 0; i < AdminConstants.TREE_MAX_DEEP + 1; i++) {
                TSysRole tSysRole = tSysRoleService.findById(id);
                if (null != tSysRole) {
                    Map<String, Long> map = Maps.newHashMap();
                    map.put("id", id);
                    if( ! roleIdList.contains(id)){
                        break;
                    }
                    id = tSysRole.getPid();
                    map.put("pid", id);
                    tSysRoleList.add(map);
                } else {
                    break;
                }
            }
            return Result.succeed(tSysRoleList);
        }
    }

    @PostMapping(value = "all")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','USER_ALL','USER_CREATE','USER_EDIT')")
    public Result getAll() {
        return Result.succeed(tSysRoleService.list());
    }

    @ApiOperation("查询角色")
    @PostMapping(value = "getList")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public Result getList(@LoginUser DefaultJwtUserDetails userDetails, @MultiRequestBody TSysRole tSysRole,
                          @MultiRequestBody PageRequest pageRequest) {
        IPage<TSysRole> roleDomainIPage = tSysRoleService.list(userDetails.getId(), tSysRole.getName(), pageRequest);
        return Result.succeed(new PageResult(roleDomainIPage));
    }

    @PostMapping(value = "create")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_CREATE')")
    public Result create(@LoginUser DefaultJwtUserDetails userDetails, @Validated @RequestBody TSysRole roleDomain) {
        return tSysRoleService.create(userDetails, roleDomain);
    }

    @PostMapping(value = "update")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public Result update(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody TSysRole domain) {
        return tSysRoleService.update(userDetails, domain);
    }

    @PostMapping(value = "permission")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public Result updatePermission(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody TSysRole domain) {
        domain.setUpdateBy(userDetails.getId());
        tSysRoleService.updatePermission(domain);
        return Result.succeed("修改角色权限成功");
    }

    @PostMapping(value = "menu")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public Result updateMenu(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody TSysRole domain) {
        domain.setUpdateBy(userDetails.getId());
        tSysRoleService.updateMenu(domain);
        return Result.succeed("修改角色菜单成功");
    }

    @PostMapping(value = "delete")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_DELETE')")
    public Result delete(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody TSysRole tSysRole) {
        return tSysRoleService.delete(userDetails, tSysRole.getId(), tSysRole.getIsForce());
    }

    @ApiOperation("查询角色树")
    @PostMapping(value = "/tree")
    public Result getRoleTree(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody(required = false)  TSysRole paramsTSysRole) {
        if(paramsTSysRole == null){
            paramsTSysRole = new TSysRole();
        }
        Long operationId = userDetails.getId();
        Boolean isCreateUser = paramsTSysRole.getIsCreateUser();
        if(AdminConstants.ADMIN_USER_ID.equals(operationId)){
            List<TSysRole> tSysMenuList = tSysRoleService.findByPid(AdminConstants.DEFAULT_PARENT_ID);
            return Result.succeed(tSysRoleService.getRoleTree(tSysMenuList));
        }else{
            TSysUser tSysUser = tSysUserService.findById(userDetails.getId());
            final Set<Long> roleIdList = new HashSet<>();
            tSysUser.getRoles().forEach(tSysRole ->{
                if(isCreateUser == null || ! isCreateUser){
                    roleIdList.add(tSysRole.getId());
                }
                tSysRoleService.getAllChildrenRoleIdListByRoleId(tSysRole.getId(), roleIdList);
            });
            List<TSysRole> tSysRoleList = new ArrayList<>();
            roleIdList.forEach(id -> tSysRoleList.add(tSysRoleService.findById(id)));
            return Result.succeed(tSysRoleService.getRoleTreeByList(tSysRoleList));
        }
    }
}