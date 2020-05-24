package com.greatwall.jhgx.rest;

import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.domain.TSysRole;
import com.greatwall.jhgx.service.TSysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

/**
 * @author wangcan
 * @date 2019/8/27 10:46
 */
@Slf4j
@RestController
public class BaseController {

    @Autowired
    private TSysRoleService tSysRoleService;

    /**
     * 权限效验
     * @param userDetails 当前完整的用户信息
     * @param permissionList 需要效验的权限列表
     * @author lt
     * @date 2020年3月24日13:39:22
     **/
    public boolean checkPermission(DefaultJwtUserDetails userDetails, String... permissionList) {
        if(userDetails == null || CollectionUtils.isEmpty(userDetails.getRoles())){
            return false;
        }
        HashSet<String> userPermissions = (HashSet<String>) userDetails.getRoles();
        for(String permission : permissionList) {
            if(userPermissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }
}
