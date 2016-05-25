package com.h3c.o2o.portal.authuser.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.h3c.o2o.portal.util.WifiUtils;

/**
 * 增删改查，公共方法实现
 * 
 * @author heqiao
 *
 * @param <T>
 */
public class CommonDaoImpl<T> extends HibernateDaoSupport implements CommonDao<T> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1751942239826399264L;

	/**
	 * log
	 */
	protected Log log = LogFactory.getLog(getClass());

	/**
	 * 批次大小
	 */
	private static final int BATCHSIZE = 25;

	/**
	 * 不会取到的ID Long.MIN = -9223372036854775808
	 */
	private static final String ILLEGALID = "-9223372036854775807";

	@Override
	public List<T> insert(List<T> newEntities, Class<T> c, String idProperty) {
		Assert.hasText(idProperty, "Id Name is required!");
		// 插入
		Integer insertNum = getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				int recordsnum = 0;
				for (int i = 0; null != newEntities && i < newEntities.size(); i++) {
					if (null == newEntities.get(i)) {
						continue;
					}
					recordsnum++;
					// 取Id
					Serializable id = session.save(newEntities.get(i));
					// Set Id
					try {
						WifiUtils.callSetMethod(newEntities.get(i), c, idProperty, new Object[] { id });
					} catch (Exception e) {
						// id设置不对，导致返回实体中无ID
						throw new HibernateException("CallSetMethod failed!", e);
					}
					if (recordsnum % BATCHSIZE == 0) {
						session.flush();
						session.clear();
					}
				}
				if (recordsnum % BATCHSIZE != 0) {
					session.flush();
					session.clear();
				}
				return recordsnum;
			}
		});
		if (log.isDebugEnabled()) {
			log.debug(insertNum + " was inserted, Entities = [ " + newEntities + " ]");
		}
		if (null != newEntities && newEntities.size() != insertNum) {
			int entityNum = null != newEntities ? newEntities.size() : 0;
			log.warn(entityNum + " entities but " + insertNum + " inserted!");
		}
		// 查询返回数据
		return newEntities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryByHql(String hql, Object... o) {
		return getHibernateTemplate().find(hql, o);
	}

	@Override
	public List<T> queryByIds(Class<T> c, String idProperty, Long... ids) {
		Assert.notEmpty(ids, "id must not be null!");
		List<T> ret = getHibernateTemplate().execute(new HibernateCallback<List<T>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				List<T> entities = new ArrayList<T>();
				final String fromPart = "from " + c.getSimpleName() + " where ";
				final String whereTemplate = idProperty + "= ";
				StringBuffer sbuilder = new StringBuffer(fromPart + whereTemplate);
				int idsNum = 0;
				for (Long id : ids) {
					idsNum++;
					sbuilder.append(id + " or " + whereTemplate);
					if (idsNum >= BATCHSIZE * 2) {
						sbuilder.append(ILLEGALID); // for grammar
						Query query = session.createQuery(sbuilder.toString());
						entities.addAll(query.list());
						// reset
						idsNum = 0;
						sbuilder = new StringBuffer(fromPart);
					}
				}
				if (idsNum > 0) {
					sbuilder.append(ILLEGALID); // for grammar
					Query query = session.createQuery(sbuilder.toString());
					entities.addAll(query.list());
				}
				return entities;
			}

		});
		return ret;
	}

	/**
	 * 通过ID删除
	 * 
	 * @param deleteEntities
	 * @param c
	 * @param idProperty
	 */
	@Override
	public void delete(List<T> deleteEntities, Class<T> c, String idProperty) {
		final String whereTemplate = idProperty + "= ";
		StringBuffer sbuffer = new StringBuffer(whereTemplate);
		int idsNum = 0;
		for (int i = 0; null != deleteEntities && i < deleteEntities.size(); i++) {
			Object o;
			try {
				o = WifiUtils.getObjectProperty(deleteEntities.get(i), idProperty);
			} catch (Exception e) {
				throw new HibernateException("Cannot get Id!", e);
			}
			Assert.isInstanceOf(Long.class, o, "Id of " + c.getClass().getSimpleName() + " must be a Long!");
			Long id = (Long) o;
			sbuffer.append(id + " or " + whereTemplate);
			idsNum++;
			if (idsNum >= 2 * BATCHSIZE) {
				// delete
				sbuffer.append(ILLEGALID); // for grammar
				delete(sbuffer.toString(), c, null);
				// reset
				idsNum = 0;
				sbuffer = new StringBuffer(whereTemplate);
			}
		}
		if (0 < idsNum) {
			sbuffer.append(ILLEGALID); // for grammar
			delete(sbuffer.toString(), c, null);
		}
	}

	@Override
	public List<T> update(List<T> updateEntities, Class<T> c, String idName, String... fields) {
		// 记录更新的ID
		List<Long> updateIds = new ArrayList<Long>();
		Integer updatedFieldsNum = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			/**
			 * 构建HQL
			 * 
			 * @param entity
			 * @param idName
			 * @param hqlArgs
			 * @param fields
			 * @return
			 * @throws Exception
			 */
			private String buildHql(T entity, String idName, Map<String, Object> hqlArgs, String... fields)
					throws Exception {
				StringBuffer sbuilder = new StringBuffer(" update " + entity.getClass().getSimpleName() + " set ");
				// get id value
				Object o = WifiUtils.getObjectProperty(entity, idName);
				Assert.isInstanceOf(Long.class, o, "Id must be a Long type!");
				Long id = (Long) o;
				updateIds.add(id);
				for (int i = 0; null != fields && i < fields.length; i++) {
					if (!StringUtils.hasText(fields[i])) {
						continue;
					}
					sbuilder.append(fields[i] + "= :_filed" + i + ",");
					// get field value
					Object value = WifiUtils.getObjectProperty(entity, fields[i]);
					hqlArgs.put("_filed" + i, value);
				}
				// remove comma
				sbuilder.setLength(sbuilder.length() - 1);
				// add where part
				sbuilder.append(" where " + idName + "=" + id);
				return sbuilder.toString();
			}

			/**
			 * 更新域
			 * 
			 * @param session
			 * @param entity
			 * @param field
			 * @throws Exception
			 */
			private void updateFields(Session session, T entity, String idName, String... fields) throws Exception {
				Map<String, Object> hqlArgs = new HashMap<String, Object>();
				String hql = buildHql(entity, idName, hqlArgs, fields);
				Query query = session.createQuery(hql);
				setHqlParameters(query, hqlArgs);
				int updateColumn = query.executeUpdate();
				if (log.isDebugEnabled()) {
					log.debug(updateColumn + " Column was updated when updateFields.");
				}
			}

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				int updatedEntityNum = 0; // 被更新实体数
				int opNum = 0;
				for (int j = 0; null != updateEntities && j < updateEntities.size(); j++) {
					if (null == updateEntities.get(j)) {
						continue;
					}
					updatedEntityNum++;
					try {
						updateFields(session, updateEntities.get(j), idName, fields);
					} catch (Exception e) {
						throw new HibernateException("update " + updateEntities.get(j) + " Field failed!", e);
					}
					opNum++;
					if (opNum % BATCHSIZE == 0) {
						session.flush();
						session.clear();
					}
				}
				if (opNum % BATCHSIZE != 0) {
					session.flush();
					session.clear();
				}
				return updatedEntityNum;
			}
		});
		if (log.isDebugEnabled()) {
			log.debug(updatedFieldsNum + " entities was updated, entities = [ " + updateEntities + " ] "
					+ " fields = [ " + fields + " ]");
		}
		// 重查一次
		if (null != updateIds && updateIds.size() > 0) {
			return queryByIds(c, idName, updateIds.toArray(new Long[] {}));
		} else {
			return null;
		}
	}

	@Override
	public void update(List<T> entities) {
		Integer updateNum = getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				int updatedEntityNum = 0; // 被更新实体数
				for (int i = 0; null != entities && i < entities.size(); i++) {
					if (null == entities.get(i)) {
						continue;
					}
					updatedEntityNum++;
					session.update(entities.get(i));
					if (updatedEntityNum % BATCHSIZE == 0) {
						session.flush();
						session.clear();
					}
				}
				if (updatedEntityNum % BATCHSIZE != 0) {
					session.flush();
					session.clear();
				}
				return updatedEntityNum;
			}
		});
		if (null != entities && !ObjectUtils.equals(updateNum, entities.size())) {
			log.warn(entities.size() + " entities, but " + updateNum + " was updated, entities: " + entities);
		}
		if (log.isDebugEnabled()) {
			log.debug(updateNum + " entities was updated, entities = [ " + entities + " ] ");
		}
	}

	@Override
	public void delete(String hqlWherePart, Class<T> c, Map<String, Object> args) {
		Integer deleteNum = getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sbuilder = new StringBuffer();
				sbuilder.append(" delete " + c.getSimpleName() + " ");
				if (StringUtils.hasText(hqlWherePart)) {
					sbuilder.append(" where " + hqlWherePart);
				}
				Query query = session.createQuery(sbuilder.toString());
				setHqlParameters(query, args);
				return query.executeUpdate();
			}
		});
		if (log.isDebugEnabled()) {
			log.debug(deleteNum + " " + c.getSimpleName() + " was delete, hqlWherePart: " + hqlWherePart);
		}
	}

	/**
	 * 设置HQL参数
	 * 
	 * @param query
	 * @param args
	 */
	private void setHqlParameters(Query query, Map<String, Object> args) {
		if (null != args && !args.isEmpty()) {
			Iterator<Entry<String, Object>> it = args.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				String paraName = entry.getKey();
				Object value = entry.getValue();
				query.setParameter(paraName, value);
			}
		}
	}

}
