package com.greatwall.jhgx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.greatwall.component.ccyl.common.model.PageRequest;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.component.ccyl.common.service.ISuperService;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.domain.TSysUser;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 后台用户信息
 * @author zsd
 **/
@CacheConfig(cacheNames = "user")
public interface TSysUserService extends ISuperService<TSysUser> {

    /**
     * get
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    TSysUser findById(long id);

    /**
     * create
     *
     * @param domain
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean create(TSysUser domain);

    /**
     * 创建用户
     * @param userDetails
     * @param domain
     * @param password
     * @return
     */
    Result createByUser(DefaultJwtUserDetails userDetails, TSysUser domain, String password);

    /**
     * update
     *
     * @param domain
     * @return
     */
    boolean update(TSysUser domain);

    /**
     * delete
     *
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 根据账号获取
     * @param userName
     * @return
     */
    TSysUser findByUserName(String userName);

    /**
     * 分页查询
     * @param entity
     * @param pageRequest
     * @param roleIdList
     * @return
     */
    IPage<TSysUser> selectByConditionToPageResult(TSysUser entity, PageRequest pageRequest,
                                                  List<Long> roleIdList);

    /**
     * 更新用户信息
     * @param operationUser
     * @param user
     * @return
     */
    Result updateByUser(DefaultJwtUserDetails operationUser, TSysUser user);

    /**
     * 修改用户密码
     * @param operationUser
     * @param id
     * @param password
     * @return
     */
    Result editUserPassword(DefaultJwtUserDetails operationUser, Long id, String password);

    /**
     * 删除
     * @param userDetails
     * @param id
     * @return
     */
    Result deleteUser(DefaultJwtUserDetails userDetails, Long id);

    /**
     * 获取用户账号
     * @param userIds
     * @return
     */
    String getUserNamesByUserIds(String userIds);

    /**
     * 获取用户昵称
     * @param id
     * @param nickNameMap
     * @return
     */
    String getNickNameById(Long id, Map<Long, String> nickNameMap);

    /**
     * 根据账号更新昵称
     * @param nickName
     * @param userName
     * @return
     */
    int updateNickNameByUserName(String nickName, String userName);
}
