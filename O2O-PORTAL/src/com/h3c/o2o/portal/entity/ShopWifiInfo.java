package com.h3c.o2o.portal.entity;
/**
 * WIFI门店实体
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
    private String shopId; // 门店ID，WXServer生成

    @Column(name = "SHOP_NAME")
    private String shopName; // 门店名称

    @Column(name = "SSID")
    private String ssid; // 无线网络设备的ssid，未添加设备为空

    @Column(name = "PROTOCOL_TYPE")
    private Integer protocolType; // 门店内设备的设备类型，0-未添加设备，1-专业型设备，4-密码型设备，5-portal自助型设备，31-portal改造型设备

    @Column(name = "SID")
    private String sid; // 商户自己的id，与门店poi_id对应关系，建议在添加门店时候建立关联关系，具体请参考“微信门店接口”

    /** 公众号 */
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
