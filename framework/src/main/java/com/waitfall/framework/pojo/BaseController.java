package com.waitfall.framework.pojo;

import cn.dev33.satoken.stp.StpUtil;

/**
 * @author by 秋
 * @date 2025/7/8 22:47
 */
public class BaseController {

    public String getUserId(){
        return StpUtil.getLoginIdAsString();
    }
}
