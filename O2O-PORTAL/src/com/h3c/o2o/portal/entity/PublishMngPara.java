package com.h3c.o2o.portal.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 *  �������������ʵ����
 * @author h12111
 *
 */
@Entity
@AccessType("field")
@Table(name = "TBL_UAM_PUBLISH_PARAM")
public class PublishMngPara implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -2925474571521165414L;

	/**
	 * ���������û���Ϣ�ϴ�ʱ����
	 */
	public static final String REPORT_INTERVAL_PARA_NAME = "USERREPORTINTERVAL";

	/**Ĭ��ֵ */
	public static final long DEFAUL_INTERVAL = 60;

	private static final List<String> paraNames = new ArrayList<String>();

	static {
		paraNames.add(REPORT_INTERVAL_PARA_NAME);
	}

	/** ����*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** ĳһ�����µĵ��̱�ʶ��������ϵͳ������*/
    @Column(name = "STORE_ID")
    private Long store_id;

    /** ������*/
    @Column(name = "PARAM_NAME")
    private String param_name;

    /** ������*/
    @Column(name = "PARAM_VALUE")
    private String param_value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

	public static ArrayList<String> getParaNames() {
		return new ArrayList<String>(paraNames);
	}

	@Override
	public String toString() {
		return "PublishMngPara [id=" + id + ", store_id=" + store_id
				+ ", param_name=" + param_name + ", param_value=" + param_value
				+ "]";
	}

}
