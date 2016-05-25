package com.h3c.o2o.portal.cache;

import java.io.Serializable;
import java.util.Set;

import com.h3c.o2o.portal.cache.update.ICacheUpdate;

/**
 * cache操作的业务层封装。
 * @author l04133
 *
 */
public interface CacheMgr<K extends Serializable, V extends Serializable> extends ICacheUpdate{

    /**
     * 缓存的初始化操作，如果需要在启动期间调用，则需要在spring配置文件中描述。
     */
    void init();


    /**
     * 添加数据，有则覆盖。
     * @param key 查找的key，独立出来方便后续使用独立的算法生成key
     * @param value 序列化值，方便后续写硬盘。
     */
    void put(K key, V value);

    /**
     * 如果不存在则添加数据。
     * @param key
     * @param value
     */
    boolean putIfAbsent(K key, V value);

    /**
     * 删除缓存。
     * @param key
     */
    void remove(K key);

    /**
     * 根据实体主键关系删除缓存。
     * @param id 实体ID
     */
    void removeByRelation(Long id);

    /**
     * 获取数据
     * @param key
     * @return
     */
    V getFromCache(K key);

    /**
     * 获取数据，与getFromCache不同，该方法在缓存中无法得到数据时，将查询数据库
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 返回所有的key，目前非key的查找先通过这种方式。
     * @return
     */
    Set<K> getKeys();

    /**
     * 清空缓存。
     */
    void clear();

}

