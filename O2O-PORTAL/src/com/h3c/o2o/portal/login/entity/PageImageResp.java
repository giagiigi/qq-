/*
 * Copyright (c) 2015, Hangzhou H3C Technologies Co., Ltd. All rights reserved.
 * <http://www.h3c.com/>
 *------------------------------------------------------------------------------
 * Product     : iMC V300R002
 * Module Name : 
 * Date Created: 2015-7-27
 * Creator     : j09980
 * Description : 
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 * YYYY-MM-DD  zhangshan        XXXX project, new code file.
 * 
 *------------------------------------------------------------------------------
 */
package com.h3c.o2o.portal.login.entity;

import java.io.Serializable;

/**
 * add description of types here
 *
 * @author j09980
 */
public class PageImageResp implements Serializable {

    /**
     * add description of field here
     */
    private static final long serialVersionUID = -1198995099609770538L;
    
    private AuthCfgResp image;

    public AuthCfgResp getImage() {
        return image;
    }

    public void setImage(AuthCfgResp image) {
        this.image = image;
    }

}
