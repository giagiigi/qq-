package com.h3c.o2o.portal.entity;
/**
 * WIFI�ŵ�ʵ��
 * @author heqiao
 *
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@AccessType("field")
@Table(name = "TBL_UAM_WIFI_SHOP_INFO")
public class ShopWifiInfo {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // ID

    @Column(name = "SHOP_ID")
    private String shopId; // �ŵ�ID��WXServer����

    @Column(name = "SHOP_NAME")
    private String shopName; // �ŵ�����

    @Column(name = "SSID")
    private String ssid; // ���������豸��ssid��δ����豸Ϊ��

    @Column(name = "PROTOCOL_TYPE")
    private Integer protocolType; // �ŵ����豸���豸���ͣ�0-δ����豸��1-רҵ���豸��4-�������豸��5-portal�������豸��31-portal�������豸

    @Column(name = "SID")
    private String sid; // �̻��Լ���id�����ŵ�poi_id��Ӧ��ϵ������������ŵ�ʱ����������ϵ��������ο���΢���ŵ�ӿڡ�

    /** ���ں� */
    @OneToOne(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "ACCOUNT_ID")
    private WeixinAccount weixinAccount;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "AP_COUNT")
    private Integer ap_count;

    @Column(name = "TEMPLATE_ID")
    private Integer template_id;

    @Column(name = "HOMEPAGE_URL")
    private String homepage_url;

    @Column(name = "bar_type")
    private Integer bar_type;

    @Column(name = "SECRETKEY")
    private String secretkey;
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Integer getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(Integer protocolType) {
        this.protocolType = protocolType;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public WeixinAccount getWeixinAccount() {
        return weixinAccount;
    }

    public void setWeixinAccount(WeixinAccount weixinAccount) {
        this.weixinAccount = weixinAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAp_count() {
        return ap_count;
    }

    public void setAp_count(Integer ap_count) {
        this.ap_count = ap_count;
    }

    public Integer getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(Integer template_id) {
        this.template_id = template_id;
    }

    public String getHomepage_url() {
        return homepage_url;
    }

    public void setHomepage_url(String homepage_url) {
        this.homepage_url = homepage_url;
    }

    public Integer getBar_type() {
        return bar_type;
    }

    public void setBar_type(Integer bar_type) {
        this.bar_type = bar_type;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

}
