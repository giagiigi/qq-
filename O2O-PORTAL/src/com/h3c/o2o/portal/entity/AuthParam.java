package com.h3c.o2o.portal.entity;

/*
 * Copyright (c) 2007-2013, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V500R003
 * Module Name : iMC
 * Date Created: 2015-6-20
 * Creator     : wkf4532
 * Description : ��֤���ù�������
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-6-20  wkf4532           MAM project, new code file.
 *
 *------------------------------------------------------------------------------
 */

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * ��֤���ù�������ʵ����
 * @author wkf4532
 *
 */
@Entity
@AccessType("field")
@Table(name = "TBL_UAM_AUTH_PARAM")
public class AuthParam implements Serializable {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 1L;
	/** */
	public static final String URL_AFTER_AUTH = "URL_AFTER_AUTH";
	
	/** */
	public static final String ONLINE_MAX_TIME = "ONLINE_MAX_TIME";
	
	/** �����ж�ʱ�������ӣ� Ĭ�� 30*/
	public static final String IDLE_CUT_TIME = "IDLE_CUT_TIME";
	
	/** �����ж��������ֽڣ�Ĭ��10240*/
	public static final String IDLE_CUT_FLOW= "IDLE_CUT_FLOW";
	
	/**  ���map��Ҫ���ڻص������ڸ����̵Ĺ�����֤�������ӳ�ʼֵ ��
	 * ����Ժ������˲���ֱ���������޸ļ��ɣ�������չ*/
	public static Map<String, String> paramMap = new TreeMap<String, String>();
	static{
		paramMap.put( URL_AFTER_AUTH,"www.h3c.com");
		paramMap.put(ONLINE_MAX_TIME,"102400");
		paramMap.put( IDLE_CUT_TIME,"30");
		paramMap.put(IDLE_CUT_FLOW ,"10240");
	}

    /** ����*/
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** ��֤���ò������ơ�*/
    @Column(name = "AUTH_PARAM_NAME")
    private String authParamName;

    /** ��֤����ֵ��*/
    @Column(name = "AUTH_PARAM_VALUE")
    private String authParamValue;

    /**
     * ��������   ��֤����ID��Ŀǰֻ��һ����¼��
     * */
    @ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "AUTH_CFG_ID")
	private AuthCfg authCfg = new AuthCfg();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthParamName() {
		return authParamName;
	}

	public void setAuthParamName(String authParamName) {
		this.authParamName = authParamName;
	}

	public String getAuthParamValue() {
		return authParamValue;
	}

	public void setAuthParamValue(String authParamValue) {
		this.authParamValue = authParamValue;
	}

    public AuthCfg getAuthCfg() {
        return authCfg;
    }

    public void setAuthCfg(AuthCfg authCfg) {
        this.authCfg = authCfg;
    }

}