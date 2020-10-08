package com.soft1851.content.center.controller;

import com.soft1851.content.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.content.center.domain.entity.User;
import com.soft1851.content.center.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class UserController {
    private final UserService userService;
    private final AsyncRestTemplate asyncRestTemplate;

    @PostMapping(value = "/bonusUpdate")
    public User bonusUpdate(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO) {
        return userService.updateBonus(userAddBonusMsgDTO);
    }

    @PostMapping(value = "/bonusUpdateAsy")
    public void bonusUpdateAsy(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO) {
        //设置Header
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        //异步发送
        ListenableFuture<ResponseEntity<UserAddBonusMsgDTO>> entity = asyncRestTemplate.postForEntity("http://user-center/user/update/bonus", httpEntity, UserAddBonusMsgDTO.class, userAddBonusMsgDTO);
        entity.addCallback(result -> log.info(result.getBody().toString()), (e) -> log.error(e.getMessage()));
    }

    @GetMapping(value = "/sayHelloAys")
    public String sayHelloAys() {
        //异步发送
        ListenableFuture<ResponseEntity<String>> entity = asyncRestTemplate.getForEntity("http://user-center/user/hello", String.class);
        entity.addCallback(result -> log.info(result.getBody()), (e) -> log.error(e.getMessage()));
        return entity.toString();
    }
}