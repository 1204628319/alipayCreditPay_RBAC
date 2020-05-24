package com.greatwall.jhgx.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * mapper 父类，注意这个类不要让 mp 扫描到（下面的方法，来源于原框架的basemaper.java）
 * @author TianLei
 */
public interface SuperMapper<T> extends BaseMapper<T> {
    /**
     * 通用插入语句
     * @param t
     * @return
     */
    public int insert(T t);
    /**
     * 通用删除语句
     * @param t
     * @return
     */
    public int delete(T t);
    /**
     * 通用查询对象集合
     * @param t
     * @return
     */
    public List<T> selectForList(T t);
    /**
     * 更新数据
     * @param t
     * @return
     */
    public int update(T t);
    /**
     * 根据对象查询
     * @param t
     * @return
     */
    public T selectByObject(T t);
}
