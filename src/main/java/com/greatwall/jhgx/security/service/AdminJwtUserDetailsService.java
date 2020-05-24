package com.greatwall.jhgx.security.service;

import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.componnet.auth.service.DefaultUserDetailsService;
import com.greatwall.jhgx.domain.TSysUser;
import com.greatwall.jhgx.service.TSysPermissionService;
import com.greatwall.jhgx.service.TSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author TianLei
 * @date 2019/06/05
 */
@Service("adminJwtUserDetailsService")
public class AdminJwtUserDetailsService extends DefaultUserDetailsService {

    @Autowired
    private TSysPermissionService tSysPermissionService;

    @Autowired
    private TSysUserService tSysUserService;

    @Override
    public DefaultJwtUserDetails loadUserByUsername(String userName) {
        TSysUser user = tSysUserService.findByUserName(userName);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException(userName + "用户不存在");
        }

        return createJwtUser(user);
    }

    public DefaultJwtUserDetails createJwtUser(TSysUser user) {
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
