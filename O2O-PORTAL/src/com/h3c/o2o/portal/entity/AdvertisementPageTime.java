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
 * 广告父页面访问时间的实体
 * @author d12150
 *
 */
@Entity
@Table(name = "TBL_UAM_ADVERTISEMENT_PARENTPAGE_TIME")
@AccessType("field")
public class AdvertisementPageTime implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 6809039658033359839L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "TEMP_ID")
    private Long tempId;
    
    @Column(name = "TYPE_ID")
    private int typeId;
    
    @Column(name = "PAGE_VISIT_TIME")
    private Long visitTime;
    
    /**
     * 场所id
     */
    @Column(name = "STORE_ID")
    private Long storeId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Long getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Long visitTime) {
        this.visitTime = visitTime;
    }
    
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

}
