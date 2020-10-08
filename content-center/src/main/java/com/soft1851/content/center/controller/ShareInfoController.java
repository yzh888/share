package com.soft1851.content.center.controller;


import com.soft1851.content.center.common.ResponseResult;
import com.soft1851.content.center.domain.dto.IdDto;
import com.soft1851.content.center.domain.dto.PageDto;
import com.soft1851.content.center.domain.dto.ShareRequestDto;
import com.soft1851.content.center.service.ShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping(value = "/share")
@Api(tags = "分享接口", value = "提供分享信息相关的Rest API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareInfoController {
    private  final ShareService shareService;

    /**
     * 查询所有的分享信息
     * @param pageDto
     * @return
     */
    @ApiOperation(value = "查询所有分享信息", notes = "查询所有分享信息")
    @PostMapping(value = "/list")
    public ResponseResult getShareInfoList(@RequestBody PageDto pageDto) {
        return ResponseResult.success(shareService.getShareInfoList(pageDto));
    }

    /**
     * 查询分享详情
     * @param idDto
     * @return
     */
    @ApiOperation(value = "查询指定分享信息", notes = "查询指定分享信息")
    @PostMapping(value = "/detail")
    public ResponseResult getShareInfoDetailById(@RequestBody IdDto idDto) {
        return ResponseResult.success(shareService.getShareVo(idDto.getId()));
    }

    @GetMapping("test")
    //在文档中不显示，隐藏该接口
    @ApiIgnore
    public String getString() {
        return "hello";
    }

    /**
     * 查询分享详情
     * @param pageDto
     * @return
     */
    @ApiOperation(value = "模糊查询分享列表信息", notes = "模糊查询分享列表信息")
    @PostMapping(value = "/keywords")
    public ResponseResult getShareInfoDetailByKeywords(@RequestBody PageDto pageDto) {
        return ResponseResult.success(shareService.getShareInfoByKeyWords(pageDto));
    }

    @ApiOperation(value = "投稿接口", notes = "投稿接口")
    @PostMapping(value = "contribute")
    public ResponseResult contributeShare(@RequestBody ShareRequestDto shareRequestDto) {
        return shareService.insertShareInfo(shareRequestDto);
    }
}
