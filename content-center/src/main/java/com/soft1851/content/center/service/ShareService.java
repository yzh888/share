package com.soft1851.content.center.service;

import com.github.pagehelper.PageInfo;
import com.soft1851.content.center.common.ResponseResult;
import com.soft1851.content.center.domain.dto.PageDto;
import com.soft1851.content.center.domain.dto.ShareAuditDTO;
import com.soft1851.content.center.domain.dto.ShareDTO;
import com.soft1851.content.center.domain.dto.ShareRequestDto;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.domain.vo.ShareVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ShareService {
    //public List<Map<String, Object>> getShareList();

    ShareDTO findById(Integer id);

    /**
     * 根据标题模糊查询某个用户的分享列表数据，title为空则为所有数据，查询结果分页
     * @param  title
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return PageInfo<share>
     */
    PageInfo<Share> query(String title, Integer pageNo, Integer pageSize, Integer userId);

    String getHello();

    ResponseResult addContribute(ShareRequestDto shareRequestDto);

    /**
     * 获取分享列表
     * @return
     */
    List<Share> getShareInfoList(PageDto pageDto);

    /**
     * 获取分享详情
     * @param id
     * @return
     */
    ShareVo getShareVo(int id);

    /**
     * 通过关键词搜索
     * @return
     */
    List<Share> getShareInfoByKeyWords(PageDto pageDto);

    /**
     * 投稿
     * @return
     */
    ResponseResult insertShareInfo(ShareRequestDto shareRequestDto);

    /**
     * 审核分享内容信息
     * @param id
     * @param shareAuditDTO
     * @return
     */
    Share audiById(Integer id, ShareAuditDTO shareAuditDTO);

    /**
     * 测试Feign调用
     * @param id
     * @param shareAuditDTO
     * @return
     */
    Share audiById1(Integer id, ShareAuditDTO shareAuditDTO);

    /**
     * 测试 AsyncRestTemplate异步
     * @param id
     * @param shareAuditDTO
     * @return
     */
    Share audiByAsyncRestTemplate(Integer id, ShareAuditDTO shareAuditDTO);

    /**
     * 测试 @Async异步
     * @param id
     * @param shareAuditDTO
     * @return
     */
    Share audiByAsync(Integer id, ShareAuditDTO shareAuditDTO);
}
