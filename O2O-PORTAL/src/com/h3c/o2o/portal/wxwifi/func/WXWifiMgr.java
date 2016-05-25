package com.h3c.o2o.portal.wxwifi.func;

import java.util.Map;

import com.h3c.o2o.portal.entity.WXWifiTemplate;
import com.h3c.oasis.o2oserver.common.ListResult;
import com.h3c.oasis.o2oserver.common.QueryInfo;

/**
 * 主题子表逻辑。
 *
 * @author hkf6496
 *
 */
public interface WXWifiMgr {

    /**
     * 通过查询主题列表
     *
     */
    public ListResult<WXWifiTemplate> queryWifiTemplateList(QueryInfo queryInfo);

    /**
     * 通过id查询主题详细信息
     *
     * @param id 微信wifi模板id
     * @return 微信wifi模板详细信息实体
     */
    public WXWifiTemplate queryWXWifiTemplate(Long id);
    
    /**
     * 校验微信wifi模板是否被发布模板使用，如果使用，则不能删除。
     *
     * @return
     */
    public boolean isExistPublish(long wxwifiId);

    /**
     * 通过name查询主题
     * @param name
     * @return
     */
    public WXWifiTemplate queryWXWifiTemplateByName(Long ownerId, String wifiName);

    /**
     * 通过店铺id查询所有微信wifi模板
     *
     * @param id 微信wifi模板id
     * @return 微信wifi模板实体
     */
    public WXWifiTemplate queryWXWifiTemplateByOwnerId(Long ownerId);

    /**
     * 判断同店铺下是否已有同名模板
     *
     */
    public Boolean isHaveSameWXWifiTemplateName(String wifiName, Long ownerId);

    /**
     * 根据sql语句删除
     *
     * @param sql 原声sql
     */
    public void deleteCallback(final String sql);
    
    //public List<WifiShopInfo> queryWifiShopByTemplateID(Long id);     
    

    /**
     * 发布文件
     *
     * @param operate 操作类型，draw/delete
     * @param subId 子表id
     * @return 基于后续升级考虑，多部署情况。
     * @throws Exception
     */
    public Map<String, Integer> publishFile(String operate, Long subId) throws Exception;

    /**
     * 删除指定微信wifi模板
     */
    public void deleteWXWifiTemplate(WXWifiTemplate wxWifiTemplate) throws Exception;

    /**
     * 保存或更新微信wifi模板
     *
     * @param theme
     */
    public void saveOrUpdateWXWifiTemplate(WXWifiTemplate wxWifiTemplate)throws Exception;

}
