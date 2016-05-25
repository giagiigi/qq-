package com.h3c.o2o.portal.cache;

import java.io.Serializable;
import java.util.Set;

import com.h3c.o2o.portal.cache.update.ICacheUpdate;

/**
 * cache������ҵ����װ��
 * @author l04133
 *
 */
public interface CacheMgr<K extends Serializable, V extends Serializable> extends ICacheUpdate{

    /**
     * ����ĳ�ʼ�������������Ҫ�������ڼ���ã�����Ҫ��spring�����ļ���������
     */
    void init();


    /**
     * ������ݣ����򸲸ǡ�
     * @param key ���ҵ�key�����������������ʹ�ö������㷨����key
     * @param value ���л�ֵ���������дӲ�̡�
     */
    void put(K key, V value);

    /**
     * �����������������ݡ�
     * @param key
     * @param value
     */
    boolean putIfAbsent(K key, V value);

    /**
     * ɾ�����档
     * @param key
     */
    void remove(K key);

    /**
     * ����ʵ��������ϵɾ�����档
     * @param id ʵ��ID
     */
    void removeByRelation(Long id);

    /**
     * ��ȡ����
     * @param key
     * @return
     */
    V getFromCache(K key);

    /**
     * ��ȡ���ݣ���getFromCache��ͬ���÷����ڻ������޷��õ�����ʱ������ѯ���ݿ�
     * @param key
     * @return
     */
    V get(K key);

    /**
     * �������е�key��Ŀǰ��key�Ĳ�����ͨ�����ַ�ʽ��
     * @return
     */
    Set<K> getKeys();

    /**
     * ��ջ��档
     */
    void clear();

}

