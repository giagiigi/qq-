package com.h3c.o2o.portal.useragent.func;

import java.util.List;

import com.h3c.o2o.portal.entity.UserAgent;

/**
 * 主题子表逻辑。
 *
 * @author ykf2685
 *
 */
public interface UserAgentMgr {

    /**
     * 通过id查询主题详细信息
     *
     * @param id 主题id
     * @return 主题详细信息实体
     */
    public UserAgent queryById(Long id);


    /**
     * 查询主题详细信息
     *
     * @return 主题详细信息实体
     */
    public List<UserAgent> queryAll();
    
    /**
     * 根据useragent获取信息。
     *
     * @param userAgent
     * @return 对象
     */
    public UserAgent parseUserAgent(String agent);
}
