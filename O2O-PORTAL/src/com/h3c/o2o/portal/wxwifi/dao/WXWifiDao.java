package com.h3c.o2o.portal.wxwifi.dao;

import java.util.List;

import com.h3c.o2o.portal.entity.WXWifiTemplate;
import com.h3c.oasis.o2oserver.common.ListResult;
import com.h3c.oasis.o2oserver.common.QueryInfo;

/**
 * 微信wifi模板。
 *
 * @author hkf6496
 *
 */
public interface WXWifiDao {

    /**
     * 通过id查询主题详细信息
     *
     * @param id 微信wifi模板id
     * @return 微信wifi模板详细信息实体
     */
    public WXWifiTemplate readWXWifiTemplate(Long id);

    /**
     * 校验微信wifi模板是否被发布模板使用，如果使用，则不能删除。
     *
     * @return
     */
    public boolean isExistPublish(long wifiId);

    /**
     * 通过name查询微信wifi模板
     * @param wifiName
     * @return
     */
    public WXWifiTemplate readWXWifiTemplate(Long ownerId, String wifiName);

    /**
     * 通过店铺id查询所有微信wifi模板
     *
     * @param id 微信wifi模板id
     * @return 微信wifi模板详细信息实体
     */
    public List<WXWifiTemplate> readWXWifiTemplateByOwnerId(Long ownerId);

    /**
     * 保存或更新微信wifi模板
     *
     * @param theme
     */
    public void saveOrUpdateWXWifiTemplate(WXWifiTemplate wXWifiTemplate);
    /**
     * 
     * add description of methods here 
     *
     * @return
     */
    //public List<WifiShopInfo> readWifiShopByTemplateID(Long id);
    /**
     * 分页查询用户访问日志
     * @param queryInfo 查询条件
     * @return 当前页面的记录
     */
    public ListResult<WXWifiTemplate> readByqueryInfo(QueryInfo queryInfo);

    /**
     * 删除指定微信wifi模板
     */
    public void deleteWXWifiTemplate(WXWifiTemplate wXWifiTemplate);

    /**
     * 判断同店铺下是否已有同名微信wifi模板
     *
     */
    public List<WXWifiTemplate> isHaveSameWXWifiTemplate(String wifiName, Long ownerId);

    /**
     * 根据sql语句删除
     *
     * @param sql 原声sql
     */
    public void deleteCallback(final String sql);

}
