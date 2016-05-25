package com.h3c.o2o.portal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;

/**
 * ����ʵ��
 * @author d12150
 *
 */
@Entity
@Table(name = "TBL_UAM_ADVERTISEMENT")
@AccessType("field")
public class Advertisement implements Serializable{

    /**
     * add description of field here
     */
    private static final long serialVersionUID = 1205114383583267818L;

    @Id
    @Column(name = "ID")
    private Long id;
    
    /**
     * ���������ap
     */
    @Column(name = "AD_AP")
    private String ap;

    /**
     * ������ڸ�ҳ���ģ��id
     */
    @Column(name = "AD_TEMP_ID")
    private Long adTempId;
    
    /**
     * ����Ƿ�ɾ��
     * 0:δɾ���� 1��ɾ��
     */
    @Column(name = "AD_IS_DELETE")
    private int adIsDelete;

    /**
     * ��������
     */
    @Column(name = "AD_NAME")
    private String name;

    /**
     *����url
     */
    @Column(name = "AD_URL")
    private String url;

    /**
     *����href
     */
    @Column(name = "AD_HREF")
    private String adHref;
    
    /**
     *���ĸ�ҳ������
     */
    @Column(name = "AD_TYPE")
    private int adType;
    
    /**
     * ���ͼƬ�ı�id
     */
    @Column(name = "AD_ELEMENT_ID")
    private String adElementId;
    
    /**
     * ����ı�
     */
    @Column(name = "AD_TEXT")
    private String adText;
    
    public String getAp() {
        return ap;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Long getAdTempId() {
        return adTempId;
    }

    public void setAdTempId(Long adTempId) {
        this.adTempId = adTempId;
    }

    public String getAdHref() {
        return adHref;
    }

    public void setAdHref(String adHref) {
        this.adHref = adHref;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }
    
    public String getAdElementId() {
        return adElementId;
    }

    public void setAdElementId(String adElementId) {
        this.adElementId = adElementId;
    }
    
    public int getAdIsDelete() {
        return adIsDelete;
    }

    public void setAdIsDelete(int adIsDelete) {
        this.adIsDelete = adIsDelete;
    }
    
    public String getAdText() {
        return adText;
    }

    public void setAdText(String adText) {
        this.adText = adText;
    }
    
}
