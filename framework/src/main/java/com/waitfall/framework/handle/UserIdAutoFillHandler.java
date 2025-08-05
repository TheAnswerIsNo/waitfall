package com.waitfall.framework.handle;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.dromara.mpe.autofill.annotation.handler.AutoFillHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;

/**
 * 全局获取用户ID
 */
@Component
public class UserIdAutoFillHandler implements AutoFillHandler<String> {

    /**
     * @param object 当前操作的数据对象
     * @param clazz  当前操作的数据对象的class
     * @param field  当前操作的数据对象上的字段
     * @return userId
     */
    @Override
    public String getVal(Object object, Class<?> clazz, Field field) {
        // 主要为解决AI对话时异步更新获取不到userId问题
        // 若当前用户拿不到，则返回原始值，
        // 手动从请求头获取
        HttpServletRequest request = getRequest();
        if (request == null) {
            field.setAccessible(true);
            try {
                return field.get(object).toString();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return StpUtil.getLoginIdAsString();
    }

    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes servletRequestAttributes)) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }
}