/*
 * Copyright (c) 2007-2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V700R001
 * Module Name :
 * Date Created: 2015-8-5
 * Creator     : ykf2685
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * 2015-8-5  ykf2685             O2O-UAM project, new code file.
 *
 *------------------------------------------------------------------------------
 */

package com.h3c.o2o.portal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * �������û���
 *
 * @author ykf2685
 *
 */
@Entity
@Table(name = "TBL_UAM_BLACK_LIST")
@AccessType("field")
public class BlackList implements Serializable {

    /**
     *���л�id
     */
    private static final long serialVersionUID = 538515214162213410L;

    /** ���������ͣ�1���ֹ����룩 **/
    public final static Integer ADD_BY_OPERATOR = Integer.valueOf(1);

    /** ���������ͣ�2�������¼�� **/
    public final static Integer UNFRIENDLY_LOGIN = Integer.valueOf(2);

    /** ������ID */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**����ID*/
    @Column(name = "STORE_ID")
    private Long storeId;

    /** ������������û�ID*/
    @Column(name = "USER_ID")
    private Long userId;

    /**�̻�ID*/
    @Column(name = "OWNER_ID", nullable = false)
	private Long ownerId;

    /** ���������ͣ�1���ֹ����룬2�������¼��3����֤ʧ�ܣ�*/
    @Column(name = "TYPE")
    private Integer type;

    /** ����Ա���� */
    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    /** ԭ������ */
    @Column(name = "DESCRIPTION")
    private String description;

    /** ������������� */
    @Column(name = "INSERT_TIME")
    private Long insertTime;

	public BlackList(Long id, Long storeId, Long userId, Long ownerId,
			Integer type, String operatorName, String description,
			Long insertTime, String userName, Integer userType) {
		this.id = id;
		this.storeId = storeId;
		this.userId = userId;
		this.ownerId = ownerId;
		this.type = type;
		this.operatorName = operatorName;
		this.description = description;
		this.insertTime = insertTime;
	}

    public BlackList() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Long insertTime) {
        this.insertTime = insertTime;
    }

	@Override
	public String toString() {
		return "BlackList [id=" + id + ", storeId=" + storeId + ", userId="
				+ userId + ", ownerId=" + ownerId + ", type=" + type
				+ ", operatorName=" + operatorName + ", description="
				+ description + ", insertTime=" + insertTime + "]";
	}

}
