package com.greatwall.jhgx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.greatwall.component.ccyl.common.exception.BusinessException;
import com.greatwall.component.ccyl.common.model.PageResult;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.component.ccyl.common.service.impl.SuperServiceImpl;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.constants.AdminConstants;
import com.greatwall.jhgx.domain.TSysMenu;
import com.greatwall.jhgx.domain.TSysRole;
import com.greatwall.jhgx.entity.MenuEntity;
import com.greatwall.jhgx.entity.MenuMetaEntity;
import com.greatwall.jhgx.mapper.TSysMenuMapper;
import com.greatwall.jhgx.service.TSysMenuService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 菜单实现类
 *
 * @author TianLei
 */
@Service
public class TSysMenuServiceImpl extends SuperServiceImpl<TSysMenuMapper, TSysMenu> implements TSysMenuService {

    /**
     * 菜单树最多层级为2
     **/
    private static final Integer MAX_MENU_TREE = 2;

    private static final String MENU_NAME = "menu_name";

    @Override
    public TSysMenu findById(long id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(TSysMenu domain) {
        boolean isSuccess = retBool(baseMapper.insert(domain));
        if(isMoreThanFive(domain.getId())){
            throw new BusinessException("菜单树不能超过两层");
        }
        return isSuccess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(TSysMenu domain) {
        if(isMoreThanFive(domain.getId())){
            throw new BusinessException("菜单树不能超过两层");
        }
        return retBool(baseMapper.updateById(domain));
    }

    private Boolean isMoreThanFive(Long id){
        Long selectId = id;
        for(int i = 0; i < MAX_MENU_TREE + 1; i++){
            TSysMenu tSysMenu = this.findById(selectId);
            if(null != tSysMenu){
                selectId = tSysMenu.getPid();
            }else{
                return false;
            }
        }
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result create(DefaultJwtUserDetails userDetails, TSysMenu domain){
        if (domain.getId() != null) {
            throw new BusinessException("不能传入id");
        }
        domain.setCreateBy(userDetails.getId());
        domain.setUpdateBy(userDetails.getId());
        if(domain.getPid() == null){
            return Result.failed("请选择上级类目");
        }
        if(StringUtils.isBlank(domain.getMenuPath())){
            return Result.failed("请输入链接地址");
        }
        QueryWrapper<TSysMenu> tSysMenuQueryWrapper = new QueryWrapper<>();
        tSysMenuQueryWrapper.eq(MENU_NAME, domain.getMenuName());
        List<TSysMenu> tSysMenuList = this.list(tSysMenuQueryWrapper);
        if(CollectionUtils.isNotEmpty(tSysMenuList)){
            throw new BusinessException("已存在相同的菜单名称");
        }
        return Result.succeed(this.create(domain));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(DefaultJwtUserDetails userDetails, TSysMenu domain){
        QueryWrapper<TSysMenu> tSysMenuQueryWrapper = new QueryWrapper<>();
        tSysMenuQueryWrapper.eq(MENU_NAME, domain.getMenuName());
        List<TSysMenu> tSysMenuList = this.list(tSysMenuQueryWrapper);
        tSysMenuList.forEach(tSysMenu -> {
            if( ! tSysMenu.getId().equals(domain.getId())){
                throw new BusinessException("已存在相同的菜单名称");
            }
        });
        if(domain.getPid().equals(domain.getId())){
            throw new BusinessException("上级类目不能为自己");
        }
        domain.setUpdateBy(userDetails.getId());
        if(this.update(domain)) {
            return Result.succeed("修改菜单成功");
        }
        return Result.failed("修改菜单失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        // 获取子菜单
        List<TSysMenu> menuList = findByPid(id);

        // 对子菜单删除进行处理
        for (TSysMenu menu : menuList) {
            baseMapper.deleteMenuRoleRelations(menu.getId());
            baseMapper.deleteById(menu.getId());
        }
        baseMapper.deleteMenuRoleRelations(id);
        return retBool(baseMapper.deleteById(id));
    }

    @Override
    public Object getMenuTree(List<TSysMenu> menuDomains) {
        List<Map<String, Object>> list = Lists.newLinkedList();
        menuDomains.sort(Comparator.comparing(TSysMenu::getSort));
        menuDomains.forEach(menuDomain -> {
                if (Objects.nonNull(menuDomain)) {
                    // 查找子菜单
                    List<TSysMenu> menuList = findByPid(menuDomain.getId());
                    // 定义结果存储
                    Map<String, Object> retMap = Maps.newHashMap();
                    retMap.put(AdminConstants.ADMIN_ID_KEY, menuDomain.getId());
                    retMap.put(AdminConstants.ADMIN_LABEL_KEY, menuDomain.getMenuName());
                    if (CollectionUtils.isNotEmpty(menuList)) {
                        retMap.put(AdminConstants.ADMIN_CHILDREN_KEY, getMenuTree(menuList));
                    }
                    list.add(retMap);
                }
            }
        );
        return list;
    }

    @Override
    public Object getMenuTreeByList(List<TSysMenu> menuDomains) {
        menuDomains.sort(Comparator.comparing(TSysMenu::getSort));
        List<TSysMenu> tSysMenuList = buildListToTree(menuDomains);
        return buildTreeByList(tSysMenuList);
    }

    private Object buildTreeByList(List<TSysMenu> tSysMenuList){
        List<Map<String, Object>> list = Lists.newLinkedList();
        tSysMenuList.forEach(menuDomain -> {
                if (Objects.nonNull(menuDomain)) {
                    Map<String, Object> retMap = Maps.newHashMap();
                    retMap.put(AdminConstants.ADMIN_ID_KEY, menuDomain.getId());
                    retMap.put(AdminConstants.ADMIN_LABEL_KEY, menuDomain.getMenuName());
                    if (CollectionUtils.isNotEmpty(menuDomain.getChildren())) {
                        retMap.put(AdminConstants.ADMIN_CHILDREN_KEY, buildTreeByList(menuDomain.getChildren()));
                    }
                    list.add(retMap);
                }
            }
        );
        return list;
    }

    private List<TSysMenu> buildListToTree(List<TSysMenu> dirs) {
        List<TSysMenu> roots = findRoots(dirs);
        List<TSysMenu> notRoots = (List<TSysMenu>) CollectionUtils
                .subtract(dirs, roots);
        for (TSysMenu root : roots) {
            root.setChildren(findChildren(root, notRoots));
        }
        return roots;
    }

    private List<TSysMenu> findRoots(List<TSysMenu> allNodes) {
        List<TSysMenu> results = new ArrayList<>();
        for (TSysMenu node : allNodes) {
            boolean isRoot = true;
            for (TSysMenu comparedOne : allNodes) {
                if (node.getPid().equals(comparedOne.getId())) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                results.add(node);
            }
        }
        return results;
    }

    private List<TSysMenu> findChildren(TSysMenu root, List<TSysMenu> allNodes) {
        List<TSysMenu> children = new ArrayList<>();
        for (TSysMenu comparedOne : allNodes) {
            if (comparedOne.getPid().equals(root.getId())) {
                children.add(comparedOne);
            }
        }
        List<TSysMenu> notChildren = (List<TSysMenu>) CollectionUtils.subtract(allNodes, children);
        for (TSysMenu child : children) {
            List<TSysMenu> tmpChildren = findChildren(child, notChildren);
            child.setChildren(tmpChildren);
        }
        return children;
    }

    @Override
    public List<TSysMenu> findByPid(long pid) {
        QueryWrapper<TSysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq(AdminConstants.ADMIN_PID_KEY, pid);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public PageResult buildTree(List<TSysMenu> menuDomains) {
        // 定义菜单树存储
        List<TSysMenu> menuTree = Lists.newArrayList();
        // 循环构建菜单树
        for (TSysMenu menuDomain : menuDomains) {
            // 如果是顶级菜单, 即父级菜单为虚拟菜单0的时候
            if (AdminConstants.DEFAULT_PARENT_ID.equals(menuDomain.getPid())) {
                menuTree.add(menuDomain);
            }
            // 迭代子菜单
            menuDomains.forEach(matchedMenu -> {
                if(matchedMenu.getPid().equals(menuDomain.getId())){
                    if(Objects.isNull(menuDomain.getChildren())){
                        menuDomain.setChildren(Lists.newArrayList());
                    }
                    menuDomain.getChildren().add(matchedMenu);
                }
            });
        }
        // 构建菜单树返回结果
        return PageResult.noSizeAndLimit(CollectionUtils.isEmpty(menuTree) ? menuDomains : menuTree, menuDomains.size());
    }

    @Override
    public List<TSysMenu> findByRoles(List<TSysRole> roles) {
        List<TSysMenu> menuContainer = Lists.newLinkedList();
        roles.forEach(role -> {
            List<TSysMenu> roleMenus = baseMapper.findByRoleId(role.getId());
            menuContainer.addAll(roleMenus);
        });
        return menuContainer;
    }

    @Override
    public List<MenuEntity> buildMenus(Set<TSysMenu> roleMenus) {
        List<MenuEntity> retMenus = Lists.newLinkedList();
        roleMenus.forEach(menuDomain -> {
                if (Objects.nonNull(menuDomain)) {
                    List<TSysMenu> menuDomains = menuDomain.getChildren();
                    MenuEntity menuEntity = new MenuEntity();
                    menuEntity.setName(menuDomain.getMenuName());
                    menuEntity.setPath(menuDomain.getMenuPath());
                    menuEntity.setSort(menuDomain.getSort());

                    // 如果不是外链
                    if (!menuDomain.getIsFrame()) {
                        if (AdminConstants.DEFAULT_PARENT_ID.equals(menuDomain.getPid())) {
                            //一级目录需要加斜杠，不然访问 会跳转404页面
                            menuEntity.setPath("/" + menuDomain.getMenuPath());
                            menuEntity.setComponent(StringUtils.isEmpty(menuDomain.getComponent()) ? "Layout" : menuDomain.getComponent());
                        } else if (StringUtils.isNotEmpty(menuDomain.getComponent())) {
                            menuEntity.setComponent(menuDomain.getComponent());
                        }
                    }

                    // 设置元数据
                    menuEntity.setMeta(new MenuMetaEntity(menuDomain.getMenuName(), menuDomain.getMenuIcon()));
                    if (CollectionUtils.isNotEmpty(menuDomains)) {
                        menuEntity.setAlwaysShow(true);
                        menuEntity.setRedirect(AdminConstants.DONT_REDIRECT_PATH);
                        menuEntity.setChildren(buildMenus(new HashSet<>(menuDomains)));
                        // 处理是一级菜单并且没有子菜单的情况
                    } else if (AdminConstants.DEFAULT_PARENT_ID.equals(menuDomain.getPid())) {
                        MenuEntity highestMenu = new MenuEntity();
                        highestMenu.setMeta(menuEntity.getMeta());
                        // 非外链
                        if (!menuDomain.getIsFrame()) {
                            highestMenu.setPath(AdminConstants.ADMIN_MAIN_PAGE_PATH);
                            highestMenu.setName(menuEntity.getName());
                            highestMenu.setComponent(menuEntity.getComponent());
                        } else {
                            highestMenu.setPath(menuDomain.getMenuPath());
                        }
                        menuEntity.setName(null);
                        menuEntity.setMeta(null);
                        menuEntity.setComponent(AdminConstants.ADMIN_LAYOUT_COMPONENT);
                        List<MenuEntity> highMenus = new ArrayList<>();
                        highMenus.add(highestMenu);
                        menuEntity.setChildren(highMenus);
                    }
                    retMenus.add(menuEntity);
                }
            }
        );
        return retMenus;
    }

    @Override
    public List<TSysMenu> selectMenusByCondition(String name) {
        QueryWrapper<TSysMenu> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), MENU_NAME, name);
        wrapper.orderByAsc("sort");
        return baseMapper.selectList(wrapper);
    }
}
