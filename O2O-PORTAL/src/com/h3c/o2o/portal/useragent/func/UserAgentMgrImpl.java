package com.h3c.o2o.portal.useragent.func;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.h3c.o2o.portal.entity.UserAgent;
import com.h3c.o2o.portal.useragent.dao.UserAgentDao;

/**
 * �����ӱ��߼���
 *
 * @author ykf2685
 *
 */
public class UserAgentMgrImpl implements UserAgentMgr {

    /**
     * ��־����
     */
    private Log log = LogFactory.getLog(getClass());

	/** ���ݷ��ʲ� */
	private UserAgentDao userAgentDao;

	/**
     * ͨ��id��ѯ������ϸ��Ϣ
     *
     * @param id ����id
     * @return ������ϸ��Ϣʵ��
     */
    public UserAgent queryById(Long id) {
        return userAgentDao.readById(id);
    }

    /**
     * ��ѯ������ϸ��Ϣ
     *
     * @return ������ϸ��Ϣʵ��
     */
    public List<UserAgent> queryAll() {
        return userAgentDao.readAll();
    }

    /**
     * ����useragent��ȡ��Ϣ��
     *
     * @param userAgent
     * @return ����
     */
    public UserAgent parseUserAgent(String agent) {
        if(StringUtils.isBlank(agent)) {
            return new UserAgent();
        }
        UserAgent userAgent = null;
        log.info("Client user-agent is : " + agent);
        agent = agent.trim().toLowerCase();

        // ��ѯ���е�ָ��
        List<UserAgent> fingers = queryAll();
        if (null != fingers && fingers.size() > 0) {
            for (UserAgent each : fingers) {
                String temp = each.getName().trim().toLowerCase();
                if (temp.contains("android")) {
                    // UC�������androidΪadr
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
