package com.waitfall.framework.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务逻辑异常 Exception
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class CommonException extends RuntimeException {

    /**
     * 业务错误码
     *
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

}
