package com.soft1851.content.center.controller;


import com.soft1851.content.center.domain.dto.ShareAuditDTO;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminController {
    private final ShareService shareService;

    @PutMapping(value = "/audit/{id}")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO shareAuditDTO) {
        //此处需要认证授权
        return this.shareService.audiById(id, shareAuditDTO);
    }

    //测试Fegin请求方法
    @PutMapping(value = "/auditByFegin/{id}")
    public Share auditByFeign(@PathVariable Integer id, @RequestBody ShareAuditDTO shareAuditDTO) {
        //此处需要认证授权
        return this.shareService.audiById1(id, shareAuditDTO);
    }

    //测试AsyncRestTemplate异步请求方法
    @PutMapping(value = "/auditByAsyncRestTemplate/{id}")
    public Share audiByAsyncRestTemplate(@PathVariable Integer id, @RequestBody ShareAuditDTO shareAuditDTO) {
        //此处需要认证授权
        return this.shareService.audiByAsyncRestTemplate(id, shareAuditDTO);
    }

    //测试AsyncRestTemplate异步请求方法
    @PutMapping(value = "/auditByAsync/{id}")
    public Share audiByAsync(@PathVariable Integer id, @RequestBody ShareAuditDTO shareAuditDTO) {
        //此处需要认证授权
        return this.shareService.audiByAsync(id, shareAuditDTO);
    }
}
