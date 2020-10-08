package com.soft1851.content.center.feignclient;

import com.soft1851.content.center.domain.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-center")
public interface TestUserCenterFeignClient {
    //多参数查询
    @GetMapping("/users/q")
    UserDto query (@SpringQueryMap UserDto userDTO);
}
