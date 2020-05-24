package com.greatwall.jhgx.security.model;

import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author TianLei
 * @date 2019/06/05
 * 返回token
 */
@Getter
@AllArgsConstructor
public class AuthenticationInfo implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 1425037055982873656L;

    private final String token;

    private final DefaultJwtUserDetails user;
}
