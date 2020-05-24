package com.greatwall.jhgx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.greatwall.component.ccyl.common.exception.BusinessException;
import com.greatwall.component.ccyl.common.exception.ParameterException;
import com.greatwall.component.ccyl.common.model.PageRequest;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.component.ccyl.common.service.impl.SuperServiceImpl;
import com.greatwall.component.ccyl.common.utils.PageQueryBuilder;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.cache.GuavaCacheService;
import com.greatwall.jhgx.constants.AdminConstants;
import com.greatwall.jhgx.domain.TSysRole;
import com.greatwall.jhgx.domain.TSysUser;
import com.greatwall.jhgx.domain.TSysUserRole;
import com.greatwall.jhgx.mapper.TSysUserMapper;
import com.greatwall.jhgx.service.TSysRoleService;
import com.greatwall.jhgx.service.TSysUserRoleService;
import com.greatwall.jhgx.service.TSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户操作实现类
 * @author TianLei
 */
@Slf4j
@Service
public class TSysUserServiceImpl extends SuperServiceImpl<TSysUserMapper, TSysUser> implements TSysUserService {

    @Autowired
    private TSysRoleService tSysRoleService;

    @Autowired
    private TSysUserRoleService tSysUserRoleService;

    /**
     * 本地缓存
     */
    @Autowired
    private GuavaCacheService guavaCacheService;

    private static final String USER_NAME_EXIT_MSG = "该账号已存在";

    @Override
    public TSysUser findById(long id) {
        TSysUser tSysUser = baseMapper.selectById(id);
        if (Objects.nonNull(tSysUser) && Objects.nonNull(tSysUser.getId())) {
            tSysUser.setRoles(tSysRoleService.findRolesByUserId(tSysUser.getId()));
        }
        return tSysUser;
    }

    @Override
    public IPage<TSysUser> selectByConditionToPageResult(TSysUser entity, PageRequest pageRequest,
                                                         List<Long> roleIdList) {
        IPage<TSysUser> pageRecord = new PageQueryBuilder<TSysUser>().getPage(pageRequest);
        pageRecord.setRecords(baseMapper.selectUserPage(entity, pageRecord, roleIdList));
        return pageRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(TSysUser domain) {
        // 新增用户
        baseMapper.insert(domain);
        return tSysUserRoleService.saveBatch(buildUserRole(domain.getId(), domain.getRoles()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(TSysUser domain) {
        return retBool(baseMapper.updateById(domain));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateByUser(DefaultJwtUserDetails operationUser, TSysUser user) {
        Long id = user.getId();
        if(AdminConstants.ADMIN_USER_ID.equals(id)){
            throw new BusinessException("不能修改超级管理员");
        }
        Long operationId = operationUser.getId();
        TSysUser existUser = this.findById(id);
        if(null == existUser){
            throw new ParameterException("未找到用户");
        }
        if(operationId.equals(id) && ! user.getEnabled() && existUser.getEnabled()){
            throw new BusinessException("不能禁用自己");
        }
        Set<Long> canUpdateRoleIdList = getAllChildrenRoleIdByUserId(operationId);
        existUser.getRoles().forEach(tSysRole -> {
            if( ! canUpdateRoleIdList.contains(tSysRole.getId())){
                throw new BusinessException("不能修改上级角色的用户");
            }
        });
        // 如果角色信息有改变则进行角色关系操作
        boolean isUpdateRole = true;
        List<TSysRole> roleDomainList = existUser.getRoles();
        List<TSysRole> paramsRoleDomainList = user.getRoles();
        if(CollectionUtils.isEmpty(roleDomainList) && CollectionUtils.isEmpty(paramsRoleDomainList)){
            isUpdateRole = false;
        }else{
            int haveCount = 0;
            for(TSysRole paramsRoleDomain : paramsRoleDomainList){
                Long paramsRoleId = paramsRoleDomain.getId();
                for(TSysRole roleDomain : roleDomainList){
                    if(paramsRoleId.equals(roleDomain.getId())){
                        haveCount++;
                        break;
                    }
                }
            }
            if(haveCount == roleDomainList.size() && haveCount == paramsRoleDomainList.size()){
                isUpdateRole = false;
            }
        }
        if(isUpdateRole){
            // 删除现有角色关系
            QueryWrapper<TSysUserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user.getId());
            tSysUserRoleService.remove(wrapper);
            // 增加用户角色关系
            tSysUserRoleService.saveBatch(buildUserRole(user.getId(), user.getRoles()));
            // 刷新几个特定的缓存信息
            String key = "role::findRolesByUserId:" + id;
            guavaCacheService.invalidate(key);
        }
        existUser.setUpdateBy(operationId);
        existUser.setNickName(user.getNickName());
        existUser.setUserEmail(user.getUserEmail());
        existUser.setUserPhone(user.getUserPhone());
        existUser.setEnabled(user.getEnabled());

        // 解除微信绑定
        if(user.isDoUntying()) {
            existUser.setWxUnionId(StringUtils.EMPTY);
            existUser.setWxOpenId(StringUtils.EMPTY);
        }

        if(retBool(baseMapper.updateById(existUser))) {
            return Result.succeed("修改成功");
        }else{
            return Result.succeed("修改失败");
        }
    }

    /**
     * 组件用户角色关系
     * @param userId  用户ID
     * @param roleDomains 角色集合
     * @return
     */
    private List<TSysUserRole> buildUserRole(long userId, List<TSysRole> roleDomains){
        // 定义用户角色关系存储
        List<TSysUserRole> userRoleDomainList = Lists.newArrayList();
        for (TSysRole roleDomain : roleDomains) {
            TSysUserRole userRoleDomain = new TSysUserRole();
            userRoleDomain.setRoleId(roleDomain.getId());
            userRoleDomain.setUserId(userId);
            userRoleDomainList.add(userRoleDomain);
        }
        return userRoleDomainList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return retBool(baseMapper.deleteById(id));
    }

    @Override
    public TSysUser findByUserName(String userName) {
        QueryWrapper<TSysUser> wrapper = new QueryWrapper<>();
        wrapper.eq(AdminConstants.USER_NAME, userName);
        TSysUser tSysUser = baseMapper.selectOne(wrapper);
        if (Objects.nonNull(tSysUser) && Objects.nonNull(tSysUser.getId())) {
            List<TSysRole> tSysRoleList = tSysRoleService.findRolesByUserId(tSysUser.getId());
            tSysUser.setRoles(tSysRoleList);
        }
        return tSysUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createByUser(DefaultJwtUserDetails userDetails, TSysUser domain, String password){
        if (domain.getId() != null) {
            throw new ParameterException("ID参数应为空");
        }
        List<TSysRole> roleIds = domain.getRoles();
        if(CollectionUtils.isEmpty(roleIds)){
            throw new ParameterException("请选择角色");
        }
        domain.setCreateBy(userDetails.getId());
        domain.setUpdateBy(userDetails.getId());
        TSysUser exitsUser = this.findByUserName(domain.getUserName());
        if( null != exitsUser){
            throw new ParameterException(USER_NAME_EXIT_MSG);
        }

        if (this.create(domain)) {
            return Result.succeed(password, "新增用户信息成功");

        } else {
            return Result.failed("新增用户信息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result editUserPassword(DefaultJwtUserDetails operationUser, Long id, String password){
        TSysUser tSysUser = this.findById(id);
        if(Objects.isNull(tSysUser)){
            return Result.failed("用户信息不存在,id:" + id);
        }
        tSysUser.setUpdateBy(operationUser.getId());
        tSysUser.setPassword(password);
        if(this.update(tSysUser)){
            return Result.succeed("修改密码成功");
        }else{
            return Result.failed("修改密码失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteUser(DefaultJwtUserDetails userDetails, Long id){
        // 查询用户详细信息
        TSysUser tSysUser = this.findById(id);
        if(Objects.isNull(tSysUser)){
            throw new ParameterException("用户信息不存在,id:" + id);
        }
        if(id.equals(userDetails.getId())){
            throw new ParameterException("不能删除自己");
        }
        // 判断是否系统管理员
        for (TSysRole roleDomain : tSysUser.getRoles()) {
            if(Boolean.TRUE.equals(roleDomain.getIsSystem())){
                throw new ParameterException("系统用户不能删除");
            }
        }
        // 获取用户能删除的角色id
        Set<Long> canDelRoleIdList = getAllChildrenRoleIdByUserId(userDetails.getId());
        tSysUser.getRoles().forEach(tSysRole -> {
            if( ! canDelRoleIdList.contains(tSysRole.getId())){
                throw new BusinessException("不能删除上级角色的用户");
            }
        });
        tSysUser.setUpdateBy(userDetails.getId());
        if (this.removeById(id)) {
            QueryWrapper<TSysUserRole> userRoleDomainQueryWrapper = new QueryWrapper<>();
            userRoleDomainQueryWrapper.eq("user_id", id);
            tSysUserRoleService.remove(userRoleDomainQueryWrapper);
            return Result.succeed("删除用户成功");
        } else {
            return Result.failed("删除用户失败");
        }
    }

    private Set<Long> getAllChildrenRoleIdByUserId(Long userId){
        Set<Long> childrenRoleIdSet = new HashSet<>();
        TSysUser operationUser = this.findById(userId);
        operationUser.getRoles().forEach(tSysRole -> {
            Long itemRoleId = tSysRole.getId();
            childrenRoleIdSet.add(itemRoleId);
            tSysRoleService.getAllChildrenRoleIdListByRoleId(itemRoleId, childrenRoleIdSet);
        });
        return childrenRoleIdSet;
    }

    /**
     * 根据用户ids查询用户名称，多个以逗号分割
     * @param userIds，多个以逗号分割
     * @return
     */
    @Override
    public String getUserNamesByUserIds(String userIds) {
        return baseMapper.getUserNamesByUserIds(userIds);
    }

    /**
     * 根据id获取用户昵称
     * @return
     */
    @Override
    public String getNickNameById(Long id, Map<Long, String> nickNameMap){
        if (nickNameMap.containsKey(id)) {
            return nickNameMap.get(id);
        }
        String nickName = StringUtils.EMPTY;
        if (AdminConstants.ADMIN_USER_ID.equals(id)) {
            nickName = AdminConstants.ADMIN_NAME;
        }else{
            TSysUser tSysUser = this.getById(id);
            if(tSysUser != null){
                nickName = tSysUser.getNickName();
            }
        }
        nickNameMap.put(id, nickName);
        return nickName;
    }

    /**
     * 根据用户名更新用户昵称
     * @param nickName
     * @param userName
     * @return
     */
    @Override
    public int updateNickNameByUserName(String nickName ,String userName) {
        return baseMapper.updateNickNameByUserName(nickName, userName);
    }
}
