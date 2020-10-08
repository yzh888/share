package com.soft1851.user.center.controller;

import com.soft1851.user.center.common.ResponseResult;
import com.soft1851.user.center.common.ResultCode;
import com.soft1851.user.center.domain.dto.LoginDto;
import com.soft1851.user.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.user.center.domain.entity.User;
import com.soft1851.user.center.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    private final UserService userService;

    @GetMapping(value = "/hello")
    public String testNacos() {
        log.info("我被调用了》》》");
        return "hello, Nacos!!!";
    }

    @GetMapping("/discovery")
    public List<ServiceInstance> getInstances() {
        return this.discoveryClient.getInstances("user-center");
    }

    @GetMapping("/call/hello")
    public String callUserCenter() {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        String targetUrl = instances.stream()
                .map(instance -> instance.getUri().toString() + "/test/hello")
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("当前没有实例！"));
        log.info("请求的目标地址：{}",instances);
        log.info("请求的目标地址：{}",targetUrl);
        return restTemplate.getForObject(targetUrl,String.class);
    }

    @GetMapping(value = "/{id}")
    public User findUserById(@PathVariable Integer id) {
        log.info("我被请求了...");
        return this.userService.findById(id);
    }

    @GetMapping("/q")
    public User query(User user){
        return user;
    }

    //登录
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginDto loginDto){
        return ResponseResult.success(userService.login(loginDto));
    }

    //修改积分
    @PutMapping("/update/bonus")
    public ResponseResult updateBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO) {
        if(userService.updateBonus(userAddBonusMsgDTO) == 1) {
            return ResponseResult.success();
        } else {
            return ResponseResult.failure(ResultCode.DATA_IS_WRONG);
        }
    }

    //修改积分
    @PostMapping("/update/bonus1")
    public int updateBonus1(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO) {
        return userService.updateBonus(userAddBonusMsgDTO);
    }



}
