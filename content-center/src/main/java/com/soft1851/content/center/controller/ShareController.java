package com.soft1851.content.center.controller;


import com.soft1851.content.center.common.ResponseResult;
import com.soft1851.content.center.domain.dto.ShareDTO;
import com.soft1851.content.center.domain.dto.ShareRequestDto;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.mapper.ShareMapper;
import com.soft1851.content.center.service.ShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping(value = "/shares")
@Api(tags = "分享接口",value = "提供和分享相关的Rest API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {
    @Resource
    private ShareMapper shareMapper;

    private final ShareService shareService;

    @Resource
    private RestTemplate restTemplate;
//    @GetMapping(value = "/one")
//    public Share getOneShare(){
//        return null;
//    }
//
//    @GetMapping("/all")
//    public List<Share> getListShare() {
//        return shareMapper.selectAll();
//    }



    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询指定id的分享详情",notes = "查询指定id的分享详情")
    public ShareDTO findById(@PathVariable Integer id) {
        return this.shareService.findById(id);
    }

    @GetMapping("/query")
    @ApiOperation(value = "分享列表",notes = "分享列表")
    public List<Share> query(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userId) throws Exception{
        if (pageSize > 100) {
            pageSize = 100;
        }
        return this.shareService.query(title, pageNo, pageSize, userId).getList();
    }


    @GetMapping(value = "/hello")
    @ApiIgnore
    public String getHello(){
        return this.shareService.getHello();
    }

    @PostMapping(value = "/contribute")
    public ResponseResult addContribute(@RequestBody ShareRequestDto shareRequestDto){
        return this.shareService.addContribute(shareRequestDto);
    }


}
