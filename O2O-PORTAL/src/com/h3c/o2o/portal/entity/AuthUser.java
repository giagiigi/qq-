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
 * 微信认证用户，包括微信连Wifi、微信公众号的认证用户
 * @author heqiao
 *
 */
@Entity
@AccessType("field")
@Table(name = "TBL_UAM_WEIXIN_AUTHUSER")
public class AuthUser implements Serializable {


	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 9216386306629581532L;
	public static final String TABLENAME = "TBL_UAM_WEIXIN_AUTHUSER";
	public static final String ID = "id";
	public static final String STORE_ID = "store_id";
	public static final String ACCOUNT_ID = "account_id";
	public static final String OWNER_ID = "owner_id";
	public static final String OPEN_ID = "open_id";
	public static final String USER_TYPE = "user_type";
	public static final String NICKNAME = "nickname";
	public static final String SEX = "sex";
	public static final String PROVINCE = "province";
	public static final String CITY = "city";
	public static final String COUNTRY = "country";
	public static final String HEADIMGURL = "headimgurl";
	public static final String UNIONID = "unionid";
	public static final String USER_MAC = "user_mac";
	public static final String TID = "tid";

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; // 主键ID

	/**
	 * 场所ID
	 */
	@Column(name = "STORE_ID", nullable = false)
	private Long store_id; // 场所ID

	/**
	 * 公众号ID
	 */
	@Column(name = "ACCOUNT_ID", nullable = false)
	private Long account_id; // 公众号ID

	/**
	 * 租户ID
	 */
	@Column(name = "OWNER_ID", nullable = false)
	private Long owner_id; // 租户ID

	/**
	 * OPEN_ID
	 */
	@Column(name = "OPEN_ID", nullable = false)
	private String open_id; // OPEN_ID

	/**
	 * OPEN_ID
	 */
	@Column(name = "USER_TYPE", nullable = false)
	private Integer user_type; // 用户类型

	/**
	 * 昵称
	 */
	@Column(name = "NICKNAME")
	private String nickname; // 用户昵称

	/**
	 * 性别
	 */
	@Column(name = "SEX")
	private Integer sex; // 用户性别

	/**
	 * 省份
	 */
	@Column(name = "PROVINCE")
	private String province; // 用户所在省份

	/**
	 * 所在城市
	 */
	@Column(name = "CITY")
	private String city;

	/**
	 * 所在国家
	 */
	@Column(name = "COUNTRY")
	private String country;

	/**
	 * 用户头像URL
	 */
	@Column(name = "HEADIMGURL")
	private String headimgurl;

	/**
	 * 微信Union机制的unionid
	 */
	@Column(name = "UNIONID")
	private String unionid;

	/**
	 * 用户MAC
	 */
	@Column(name = "USER_MAC", nullable = false)
	private String user_mac;

	/**
	 * 加密后的手机号
	 */
	@Column(name = "TID")
	private String tid;

	@Override
	public String toString() {
		return "AuthUser [id=" + id + ", store_id=" + store_id + ", account_id=" + account_id + ", owner_id=" + owner_id
				+ ", open_id=" + open_id + ", user_type=" + user_type + ", nickname=" + nickname + ", sex=" + sex
				+ ", province=" + province + ", city=" + city + ", country=" + country + ", headimgurl=" + headimgurl
				+ ", unionid=" + unionid + ", user_mac=" + user_mac + ", tid=" + tid + "]";
	}

	// getter and setter
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

	public Long getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}

	public Long getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(Long owner_id) {
		this.owner_id = owner_id;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public Integer getUser_type() {
		return user_type;
	}

	public void setUser_type(Integer user_type) {
		this.user_type = user_type;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getUser_mac() {
		return user_mac;
	}

	public void setUser_mac(String user_mac) {
		this.user_mac = user_mac;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

}

