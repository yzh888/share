package com.soft1851.content.center.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonusMsgDTO {
    /**
     * 为谁加的积分
     */
    private Integer userId;
    /**
     * 加多少积分
     */
    private Integer bonus;

}