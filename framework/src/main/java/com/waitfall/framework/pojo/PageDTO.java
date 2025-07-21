package com.waitfall.framework.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/7/14 17:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {

    private static final Integer PAGE_NUMBER = 1;

    private static final Integer PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE_NONE = -1;

    @NotNull(message = "页码不能为空")
    @Min(value = 1L, message = "页码不能小于1")
    @Schema(description = "页码", example = "1")
    private Integer current = PAGE_NUMBER;

    @NotNull(message = "每页大小不能为空")
    @Min(value = 1L, message = "每页大小不能小于1")
    @Max(value = 100L, message = "每页大小不能大于100")
    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = PAGE_SIZE;

}
