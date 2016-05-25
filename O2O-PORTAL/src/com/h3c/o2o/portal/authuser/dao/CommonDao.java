package com.h3c.o2o.portal.authuser.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 增删改查，公共方法
 * 
 * @author heqiao
 *
 * @param <T>
 */
public interface CommonDao<T> extends Serializable {
	/**
	 * 增加
	 * 
	 * @param newEntities
	 *            插入实体
	 * @param idName
	 *            id名称
	 */
	public List<T> insert(List<T> newEntities, Class<T> c, String idProperty);

	/**
	 * 删除
	 * 
	 * @param deleteEntities
	 */
	public void delete(List<T> deleteEntities, Class<T> c, String idProperty);

	/**
	 * 删除
	 * 
	 * @param hqlPart
	 */
	public void delete(String hqlWherePart, Class<T> c, Map<String, Object> args);

	/**
	 * 修改
	 * 
	 * @param updateEntities
	 * @param fields
	 */
	public List<T> update(List<T> updateEntities, Class<T> c, String idName, String... fields);

	/**
	 * 修改
	 * @param entities
	 */
	public void update(List<T> entities);
	
	/**
	 * 查询
	 * 
	 * @param hqlPart
	 * @return
	 */
	public List<T> queryByHql(String hql, Object... o);

	/**
	 * 查询
	 * 
	 * @param c
	 * @param ids
	 * @return
	 */
	public List<T> queryByIds(Class<T> c, String idProperty, Long... ids);
}
