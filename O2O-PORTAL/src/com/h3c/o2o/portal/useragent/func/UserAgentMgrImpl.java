package com.h3c.o2o.portal.useragent.func;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.entity.UserAgent;
import com.h3c.o2o.portal.useragent.dao.UserAgentDao;

/**
 * 主题子表逻辑。
 *
 * @author ykf2685
 *
 */
public class UserAgentMgrImpl implements UserAgentMgr {

    /**
     * 日志对象
     */
    private Log log = LogFactory.getLog(getClass());

	/** 数据访问层 */
	private UserAgentDao userAgentDao;

	/**
     * 通过id查询主题详细信息
     *
     * @param id 主题id
     * @return 主题详细信息实体
     */
    public UserAgent queryById(Long id) {
        return userAgentDao.readById(id);
    }

    /**
     * 查询主题详细信息
     *
     * @return 主题详细信息实体
     */
    public List<UserAgent> queryAll() {
        return userAgentDao.readAll();
    }

    /**
     * 根据useragent获取信息。
     *
     * @param userAgent
     * @return 对象
     */
    public UserAgent parseUserAgent(String agent) {
        if(StringUtils.isBlank(agent)) {
            return new UserAgent();
        }
        UserAgent userAgent = null;
        log.info("Client user-agent is : " + agent);
        agent = agent.trim().toLowerCase();

        // 查询所有的指纹
        List<UserAgent> fingers = queryAll();
        if (null != fingers && fingers.size() > 0) {
            for (UserAgent each : fingers) {
                String temp = each.getName().trim().toLowerCase();
                if (temp.contains("android")) {
                    // UC浏览器下android为adr
                    temp = temp.replaceAll("android", "adr");
                }
                if (agent.contains(each.getName().toLowerCase().trim())
                    || agent.contains(temp)) {
                    userAgent = each;
                    break;
                }
            }
        }
        if(userAgent == null) {
            userAgent = new UserAgent();
        }
        return userAgent;
    }

    public UserAgentDao getUserAgentDao() {
        return userAgentDao;
    }

    public void setUserAgentDao(UserAgentDao userAgentDao) {
        this.userAgentDao = userAgentDao;
    }


}
