package com.greatwall.jhgx.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.greatwall.component.ccyl.common.exception.BusinessException;
import com.greatwall.component.ccyl.common.model.PageRequest;
import com.greatwall.component.ccyl.common.model.PageResult;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.component.ccyl.common.utils.CcylStringUtil;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 用户管理
 *
 * @author TianLei
 * @date 2019-06-20
 */
@RestController
@RequestMapping("admin")
@Api(tags = "用户")
public class AdminUserController {

    @Autowired
    private TSysUserService tSysUserService;

    @Autowired
    private TSysRoleService tSysRoleService;

    @PostMapping(value = "/users")
    @ApiOperation("查询用户列表")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    public Result getUsers(@LoginUser DefaultJwtUserDetails userDetails, @MultiRequestBody TSysUser domain,
                           @MultiRequestBody PageRequest pageRequest) {

        List<Long> roleIdList = tSysRoleService.getChildrenRoleIdListByUserId(userDetails.getId(), true);
        IPage<TSysUser> userDomainIPage = tSysUserService.selectByConditionToPageResult(domain, pageRequest, roleIdList);
        for(TSysUser userDomain : userDomainIPage.getRecords()){
            userDomain.setRoles(tSysRoleService.findRolesByUserId(userDomain.getId()));
        }
        return Result.succeed(new PageResult(userDomainIPage));
    }

    /**
     * 获取用户信息
     * @return
     */
    @ApiOperation("获取用户信息")
    @PostMapping("/getUserInfo")
    public Result getUserInfo(@LoginUser(isFull = true) DefaultJwtUserDetails userDetails){
        Map<String, Object> map = CcylStringUtil.objToMap(userDetails);
        TSysUser tSysUser = tSysUserService.findByUserName(userDetails.getUsername());
        map.put("nickName", tSysUser.getNickName());
        map.put("roles", userDetails.getRoles());
        return Result.succeed(map);
    }

    @ApiOperation("新增用户")
    @PostMapping(value = "/createUser")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
    public Result createUser(@LoginUser(isFull = true) DefaultJwtUserDetails userDetails, @RequestBody TSysUser domain) {
        List<TSysRole> roles = domain.getRoles();
        String password = AdminConstants.DEF_ADMIN_PASSWORD;
        if(checkNotAdmin(roles)){
            password = AdminConstants.DEF_USER_PASSWORD;
        }
        domain.setPassword(password);
        return tSysUserService.createByUser(userDetails, domain, password);
    }

    @ApiOperation("更新用户")
    @PostMapping(value = "/updateUser")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    public Result updateUser(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody TSysUser domain) {
        return tSysUserService.updateByUser(userDetails, domain);
    }

    @ApiOperation("删除用户")
    @PostMapping(value = "/deleteUser")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_DELETE')")
    public Result deleteUser(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody TSysUser tSysUser) {
        return tSysUserService.deleteUser(userDetails, tSysUser.getId());
    }

    @ApiOperation("重置密码")
    @PostMapping(value = "/user/resetPassword")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_REST_PWD')")
    public Result resetPassword(@LoginUser DefaultJwtUserDetails userDetails, @RequestBody TSysUser tSysUser) {
        String password = null;
        List<TSysRole> roles = tSysRoleService.findRolesByUserId(tSysUser.getId());
        if(checkNotAdmin(roles)){
            password = AdminConstants.DEF_USER_PASSWORD;
        }else {
            password = AdminConstants.DEF_ADMIN_PASSWORD;
        }
        return tSysUserService.editUserPassword(userDetails, tSysUser.getId(), password);
    }

    @ApiOperation("查询旧密码")
    @PostMapping(value = "/getOldPass")
    public Result getOldPass(@LoginUser(isFull = true) DefaultJwtUserDetails userDetails, @RequestBody TSysUser tSysUser){
        if (! tSysUser.getPassword().equals(userDetails.getPassword())) {
            throw new BusinessException("密码错误");
        }
        return Result.succeed("密码正确");
    }

    @ApiOperation("修改用户密码")
    @PostMapping(value = "/updatePass")
    public Result updatePass(@LoginUser(isFull = true) DefaultJwtUserDetails userDetails, @RequestBody TSysUser tSysUser){
        Long id = userDetails.getId();
        return tSysUserService.editUserPassword(userDetails, id, tSysUser.getPassword());
    }

    private  boolean checkNotAdmin(List<TSysRole> roles){
        boolean notAdmin= true;
        for (TSysRole role: roles ) {
            if(AdminConstants.ADMIN_ROLE_ID.equals(role.getId())){
                notAdmin = false;
                break;
            }
        }
        return notAdmin;
    }
}
