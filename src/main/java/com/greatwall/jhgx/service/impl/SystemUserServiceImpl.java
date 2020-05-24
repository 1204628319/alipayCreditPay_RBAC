package com.greatwall.jhgx.service.impl;

import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.componnet.auth.service.SystemUserService;
import com.greatwall.jhgx.domain.TSysUser;
import com.greatwall.jhgx.service.TSysPermissionService;
import com.greatwall.jhgx.service.TSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现该接口即可做用户信息注入
 * @author zsd
 **/
@Service
public class SystemUserServiceImpl implements SystemUserService {

    @Autowired
    private TSysUserService tSysUserService;

    @Autowired
    private TSysPermissionService tSysPermissionService;

    @Override
    public DefaultJwtUserDetails selectByUsername(String userName) {
        TSysUser user = tSysUserService.findByUserName(userName);
        return new DefaultJwtUserDetails(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.getUserSex(),
                tSysPermissionService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateAt()
        );
    }
}
