package com.soft1851.content.center.controller;


import com.soft1851.content.center.domain.entity.Notice;
import com.soft1851.content.center.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/notice")
@Api(tags = "公告接口",value = "提供公告相关的Rest API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping(value = "/one")
    @ApiOperation(value = "查询最新的一条公告",notes = "查询最新一条公告")
    public Notice getTopNotice(){
        return this.noticeService.getLatest();
    }
}
