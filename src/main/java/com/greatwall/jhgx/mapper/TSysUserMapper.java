package com.greatwall.jhgx.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.greatwall.component.ccyl.db.mapper.SuperMapper;
import com.greatwall.jhgx.domain.TSysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 后台用户信息Mapper
 * @author xuxiangke
 **/
public interface TSysUserMapper extends SuperMapper<TSysUser> {
    /**
     * 分页查询
     * @param userDomain
     * @param page
     * @param roleIdList
     * @return
     */
    List<TSysUser> selectUserPage(@Param("user") TSysUser userDomain,
                                  IPage page, @Param("roleIds") List<Long> roleIdList);

    /**
     * 获取用户id的登录账号
     * @param userIds
     * @return
     */
    String getUserNamesByUserIds(String userIds);

    /**
     * 根据账号更新昵称
     * @param nickName
     * @param userName
     * @return
     */
    int updateNickNameByUserName(@Param("nickName") String nickName, @Param("userName") String userName);
}
