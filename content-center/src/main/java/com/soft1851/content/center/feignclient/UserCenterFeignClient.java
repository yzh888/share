package com.soft1851.content.center.feignclient;


import com.soft1851.content.center.configuration.UserCenterFeignConfiguration;
import com.soft1851.content.center.domain.dto.UserAddBonusMsgDTO;

import com.soft1851.content.center.domain.dto.UserDto;
import com.soft1851.content.center.domain.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

//指定对应服务器名称
@FeignClient(name = "user-center",configuration = UserCenterFeignConfiguration.class)
public interface UserCenterFeignClient {

    @GetMapping("/users/{id}")
    UserDto findUserById(@PathVariable Integer id);

    @GetMapping("/user/hello")
    String getHello();

    /**
     * http://user-center/user/update/bonus
     * @param
     * @return
     */
    @GetMapping("/user/update/bonus")
    User updateBonus(@RequestBody UserAddBonusMsgDTO updateBonus);

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    UserDto getUserDtoById(@PathVariable int id);

    /**
     * 根据用户id查询用户信息
     * @param userAddBonusMsgDTO
     * @return
     */
    @PutMapping("/user/update/bonus")
    UserDto updateUserBonusByUserId(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO);
}