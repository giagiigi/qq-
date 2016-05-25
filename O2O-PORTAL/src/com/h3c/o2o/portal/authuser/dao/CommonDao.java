package com.h3c.o2o.portal.authuser.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * ��ɾ�Ĳ飬��������
 * 
 * @author heqiao
 *
 * @param <T>
 */
public interface CommonDao<T> extends Serializable {
	/**
	 * ����
	 * 
	 * @param newEntities
	 *            ����ʵ��
	 * @param idName
	 *            id����
	 */
	public List<T> insert(List<T> newEntities, Class<T> c, String idProperty);

	/**
	 * ɾ��
	 * 
	 * @param deleteEntities
	 */
	public void delete(List<T> deleteEntities, Class<T> c, String idProperty);

	/**
	 * ɾ��
	 * 
	 * @param hqlPart
	 */
	public void delete(String hqlWherePart, Class<T> c, Map<String, Object> args);

	/**
	 * �޸�
	 * 
	 * @param updateEntities
	 * @param fields
	 */
	public List<T> update(List<T> updateEntities, Class<T> c, String idName, String... fields);

	/**
	 * �޸�
	 * @param entities
	 */
	public void update(List<T> entities);
	
	/**
	 * ��ѯ
	 * 
	 * @param hqlPart
	 * @return
	 */
	public List<T> queryByHql(String hql, Object... o);

	/**
	 * ��ѯ
	 * 
	 * @param c
	 * @param ids
	 * @return
	 */
	public List<T> queryByIds(Class<T> c, String idProperty, Long... ids);
}
