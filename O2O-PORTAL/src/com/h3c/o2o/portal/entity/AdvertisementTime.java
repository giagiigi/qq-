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
 * ������ʱ���ʵ��
 * @author d12150
 *
 */
@Entity
@Table(name = "TBL_UAM_ADVERTISEMENT_TIME")
@AccessType("field")
public class AdvertisementTime implements Serializable{

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 1536597135781499689L;
   
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * ����id
     */
    @Column(name = "AD_ID")
    private Long advertisementId;
    /**
     * ������ʱ��
     */
    @Column(name = "AD_VISIT_TIME")
    private Long visitTime;
    /**
     * �����ʱ��
     */
    @Column(name = "AD_CLICK_TIME")
    private Long clickTime;
    
    /**
     * �Ƿ��������
     * 0����ʾû�е����1����ʾ��������
     */
    @Column(name = "AD_CLICK")
    private int click;
    
    @Column(name = "AD_MAC")
    private String mac;
    
    /**
     * ����uv
     * 0����ʾ�Ѿ����ʹ���1����ʾ�״η���
     */
    @Column(name = "AD_PU")
    private int adPu;
    
    /**
    * ����Id
    */
    @Column(name = "STORE_ID")
    private Long storeId;
    
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }

    public Long getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Long visitTime) {
        this.visitTime = visitTime;
    }

    public Long getClickTime() {
        return clickTime;
    }

    public void setClickTime(Long clickTime) {
        this.clickTime = clickTime;
    }


    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getAdPu() {
        return adPu;
    }

    public void setAdPu(int adPu) {
        this.adPu = adPu;
    }    
    
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
