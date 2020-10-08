package com.soft1851.content.center.domain.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareRequestDto {
    private Integer userId;
    private String author;
    private String downloadUrl;
    private Boolean isOriginal;
    private Integer price;
    private String summary;
    private String title;
}
