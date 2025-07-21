package com.waitfall.framework.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/7/14 17:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> {

    @Schema(description = "页码")
    private Integer pageNumber;

    @Schema(description = "每页条数")
    private Integer pageSize;

    @Schema(description = "总页数")
    private Integer totalPage;

    @Schema(description = "总条数")
    private Long totalCount;

    @Schema(description = "内容")
    private T data;

    public PageVO(Integer pageNumber, Integer pageSize, Long totalCount, T data) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.data = data;
        this.totalPage = (int)Math.ceil((double)totalCount / (double)pageSize);
    }
}
