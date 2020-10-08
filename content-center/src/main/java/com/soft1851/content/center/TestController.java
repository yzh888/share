package com.soft1851.content.center;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import com.soft1851.content.center.domain.dto.UserDto;
import com.soft1851.content.center.feignclient.TestBaiduFeignClient;
import com.soft1851.content.center.feignclient.TestUserCenterFeignClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/test")
@Api(tags = "测试接口",value = "提供测试相关的Rest API")
public class TestController {
    @Autowired
    private TestUserCenterFeignClient testUserCenterFeignClient;
    @Autowired
    private TestBaiduFeignClient testBaiduFeignClient;

    @GetMapping(value = "/test-q")
    public UserDto query(UserDto userDTO){
        return testUserCenterFeignClient.query(userDTO);
    }

    @GetMapping(value = "/baidu")
    public String baiduIndex(){
        return this.testBaiduFeignClient.index();
    }

    @GetMapping("byResource")
    @SentinelResource(value = "byResource",blockHandler = "handleException")
    public String byResource(){
        return "按名称限流";
    }

    public String handleExeption(BlockException blockException){
        return "服务不可用";
    }


}
