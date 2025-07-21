package com.waitfall.framework.pojo;

import cn.dev33.satoken.stp.StpUtil;

/**
 * @author by 秋
 * @date 2025/7/8 22:48
 */
public class BaseService {

    public String getUserId(){
        return StpUtil.getLoginIdAsString();
    }
}
