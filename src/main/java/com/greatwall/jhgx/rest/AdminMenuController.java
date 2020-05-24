package com.greatwall.jhgx.rest;

import com.greatwall.component.ccyl.common.model.PageResult;
import com.greatwall.component.ccyl.common.model.Result;
import com.greatwall.componnet.auth.annotation.LoginUser;
import com.greatwall.componnet.auth.security.DefaultJwtUserDetails;
import com.greatwall.jhgx.constants.AdminConstants;
import com.greatwall.jhgx.domain.TSysMenu;
import com.greatwall.jhgx.domain.TSysUser;
import com.greatwall.jhgx.entity.MenuEntity;
import com.greatwall.jhgx.mapper.TSysMenuMapper;
import com.greatwall.jhgx.service.TSysMenuService;
import com.greatwall.jhgx.service.TSysRoleService;
import com.greatwall.jhgx.service.TSysUserService;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * 菜单管理
 *
 * @author TianLei
 * @date 2019-06-20
 */
@RestController
@RequestMapping("admin")
@Api(tags = "菜单模块api")
public class AdminMenuController {

    @Autowired
    private TSysMenuService tSysMenuService;

    @Autowired
    private TSysMenuMapper tSysMenuMapper;

    @Autowired
    private TSysRoleService tSysRoleService;

    @Autowired
    private TSysUserService tSysUserService;

    /**
     * 构建前端路由所需要的菜单
     *
     * @return
     */
    @PostMapping(value = "/menus/build")
    public Result buildMenus(@LoginUser(isFull = true) DefaultJwtUserDetails userDetails) {
        List<TSysMenu> userMenus = tSysMenuService.findByRoles(tSysRoleService.findRolesByUserId(userDetails.getId()));
        List<TSysMenu> menuTree = (List<TSysMenu>) tSysMenuService.buildTree(userMenus).getData();
        List<MenuEntity> menuEntityList = tSysMenuService.buildMenus(new HashSet<>(menuTree));
        menuEntityList.sort(Comparator.comparing(MenuEntity::getSort));
        menuEntityList.forEach(menuEntity -> {
            List<MenuEntity> children = menuEntity.getChildren();
            if(CollectionUtils.isNotEmpty(children)){
                children.sort(Comparator.comparing(MenuEntity::getSort));
            }
            menuEntity.setName(menuEntity.getPath());
            children.forEach(menuEntity1 ->  menuEntity1.setName(menuEntity1.getPath()));
        });
        return Result.succeed(menuEntityList);
    }

    /**
     * 返回全部的菜单
     *
     * @return
     */
    @PostMapping(value = "/menus/tree")
    public Result getMenuTree(@LoginUser DefaultJwtUserDetails userDetails) {
        Long operationId = userDetails.getId();
        if(AdminConstants.ADMIN_USER_ID.equals(operationId)){
            List<TSysMenu> tSysMenuList = tSysMenuService.findByPid(AdminConstants.DEFAULT_PARENT_ID);
            return Result.succeed(tSysMenuService.getMenuTree(tSysMenuList));
        }else{
            TSysUser tSysUser = tSysUserService.findById(userDetails.getId());
            final List<TSysMenu> finalMenuList = new ArrayList<>();
            tSysUser.getRoles().forEach(tSysRole ->finalMenuList.addAll(tSysMenuMapper.findByRoleId(tSysRole.getId())));
            return Result.succeed(tSysMenuService.getMenuTreeByList(finalMenuList));
        }
    }

    @PostMapping(value = "/getMenusList")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_SELECT')")
    public Result getMenus(@RequestBody TSysMenu tSysMenu) {
        List<TSysMenu> menuDomains = tSysMenuService.selectMenusByCondition(tSysMenu.getName());
        PageResult pageResult = tSysMenuService.buildTree(menuDomains);
        List<TSysMenu> menuTree = (List<TSysMenu>) pageResult.getData();
        //查找、添加在查询结果中，但是没有在菜单树中的菜单
        for(TSysMenu notAddMenu : menuDomains) {
            if(!isFindMenus(notAddMenu, menuTree, false)) {
                menuTree.add(notAddMenu);
            }
        }
        menuTree.sort(Comparator.comparing(TSysMenu::getSort));
        return Result.succeed(PageResult.noSizeAndLimit(menuTree, menuTree.size()));
    }

    private boolean isFindMenus(TSysMenu notAddMenu, List<TSysMenu> menuTree, boolean isExist) {
        for(TSysMenu menu : menuTree) {
            if(notAddMenu.getId().equals(menu.getId())) {
                return true;
            } else if(CollectionUtils.isNotEmpty(menu.getChildren())&& isFindMenus(notAddMenu, menu.getChildren(), isExist)) {
                    return true;
            }else{
                //军规要求
            }
        }
        return false;
    }

    @PostMapping(value = "/menus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_CREATE')")
    public Result create(@LoginUser DefaultJwtUserDetails userDetails, @Valid @Validated @RequestBody TSysMenu domain) {
        return tSysMenuService.create(userDetails, domain);
    }

    @PostMapping(value = "/editMenus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_EDIT')")
    public Result update(@LoginUser DefaultJwtUserDetails userDetails, @Valid @RequestBody TSysMenu domain) {
        return tSysMenuService.update(userDetails, domain);
    }

    @PostMapping(value = "/deleteMenus")
    @PreAuthorize("hasAnyRole('ADMIN','MENU_ALL','MENU_DELETE')")
    public Result delete(@RequestBody TSysMenu tSysMenu) {
        if(tSysMenuService.delete(tSysMenu.getId())) {
            return Result.succeed("删除菜单成功");
        }
        return Result.failed("删除菜单失败");
    }
}
