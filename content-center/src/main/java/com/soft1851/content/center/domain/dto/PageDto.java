package com.soft1851.content.center.domain.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页Dto")
public class PageDto {
    @ApiModelProperty(name = "pageIndex", value = "当前页数")
    private Integer pageIndex;
    @ApiModelProperty(name = "pageSize", value = "每页的条数")
    private Integer pageSize;
    @ApiModelProperty(name = "keywords", value = "搜索关键词")
    private String keywords;
    @ApiModelProperty(name = "userId", value = "用户id")
    private Integer userId;
}
