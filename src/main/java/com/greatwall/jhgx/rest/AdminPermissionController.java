package com.greatwall.jhgx.rest;

import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.componnet.auth.annotation.LoginUser;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.constants.AdminConstants;
import com.greatwall.jhgx.domain.TSysPermission;
import com.greatwall.jhgx.domain.TSysUser;
import com.greatwall.jhgx.mapper.TSysPermissionMapper;
import com.greatwall.jhgx.service.TSysPermissionService;
import com.greatwall.jhgx.service.TSysUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理
 *
 * @author TianLei
 * @date 2019-06-20
 */
@RestController
@RequestMapping("admin")
@Api(tags = "权限模块api")
public class AdminPermissionController {

    @Autowired
    private TSysPermissionService tSysPermissionService;

    @Autowired
    private TSysUserService tSysUserService;

    @Autowired
    private TSysPermissionMapper tSysPermissionMapper;

    /**
     * 返回全部的权限，新增角色时下拉选择
     *
     * @return
     */
    @PostMapping(value = "/permissions/tree")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_CREATE','PERMISSION_EDIT','ROLES_SELECT','ROLES_ALL')")
    public Result getTree(@LoginUser DefaultJwtUserDetails userDetails) {
        Long operationId = userDetails.getId();
        if(AdminConstants.ADMIN_USER_ID.equals(operationId)){
            List<TSysPermission> tSysMenuList = tSysPermissionService.findByPid(AdminConstants.DEFAULT_PARENT_ID);
            return Result.succeed(tSysPermissionService.getPermissionTree(tSysMenuList));
        }else{
            TSysUser tSysUser = tSysUserService.findById(userDetails.getId());
            final List<TSysPermission> finalMenuList = new ArrayList<>();
            tSysUser.getRoles().forEach(tSysRole -> finalMenuList.addAll(tSysPermissionMapper.findByRoleId(tSysRole.getId())));
            return Result.succeed(tSysPermissionService.getMenuTreeByList(finalMenuList));
        }
    }

    @PostMapping(value = "/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_SELECT')")
    public Result getPermissions(@RequestBody TSysPermission tSysPermission) {
        List<TSysPermission> permissionDomains = tSysPermissionService.selectPermissionsByCondition(tSysPermission.getName());
        return Result.succeed(tSysPermissionService.buildTree(permissionDomains));
    }

    @PostMapping(value = "/createPermissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_CREATE')")
    public Result create(@LoginUser DefaultJwtUserDetails userDetails, @Valid @Validated @RequestBody TSysPermission domain) {
        return tSysPermissionService.create(userDetails, domain);
    }

    @PostMapping(value = "/editPermissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_EDIT')")
    public Result update(@LoginUser DefaultJwtUserDetails userDetails, @Valid @RequestBody TSysPermission domain) {
        return tSysPermissionService.update(userDetails, domain);
    }

    @PostMapping(value = "/deletePermissions")
    @PreAuthorize("hasAnyRole('ADMIN','PERMISSION_ALL','PERMISSION_DELETE')")
    public Result delete(@RequestBody TSysPermission tSysPermission) {
        if(tSysPermissionService.delete(tSysPermission.getId())) {
            return Result.succeed("删除权限成功");
        }
        return Result.failed("删除权限失败");
    }
}
