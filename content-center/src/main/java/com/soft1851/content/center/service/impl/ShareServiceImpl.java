package com.soft1851.content.center.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.soft1851.content.center.common.ResponseResult;
import com.soft1851.content.center.common.ResultCode;
import com.soft1851.content.center.domain.dto.*;
import com.soft1851.content.center.domain.entity.MidUserShare;
import com.soft1851.content.center.domain.entity.Share;
import com.soft1851.content.center.domain.enums.AuditStatusEnum;
import com.soft1851.content.center.domain.vo.ShareVo;
import com.soft1851.content.center.feignclient.UserCenterFeignClient;
import com.soft1851.content.center.mapper.MidUserShareMapper;
import com.soft1851.content.center.mapper.ShareMapper;
import com.soft1851.content.center.service.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareServiceImpl implements ShareService {
    private final ShareMapper shareMapper;
    private final MidUserShareMapper midUserShareMapper;
    private final UserCenterFeignClient userCenterFeignClient;
    private final RocketMQTemplate rocketMQTemplate;
    private final AsyncRestTemplate asyncRestTemplate;

    @Resource
    private RestTemplate restTemplate;

//    @Override
//    public List<Map<String, Object>> getShareList() {
//        List<Map<String, Object>> shareList = null;
//        List<Share> shares = shareMapper.selectAll();
//        for(int i = 0; i < shares.size(); i++) {
//            //Share share = shares.get(i);
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("share", shares);
//            shareList.add(map);
//
//        }
//        return shareList;
//    }

    @Override
    public ShareDTO findById(Integer id) {
        // 获取分享实体
        Share share = this.shareMapper.selectByPrimaryKey(id);
        // 获得发布人id
        Integer userId = share.getUserId();

        // 1.代码不可读
        // 2.复杂的url难以维护: https://user-center/s?ie={ie}&f={f}&rsv_bp=1&rsv_idx=1&tn=baidu&wd=a&rsv_pq=c86459bd002cfbaa&rsv_t=edb19hb%2BvO%2BTySu8dtmbl%2F9dCK%2FIgdyUX%2BxuFYuE0G08aHH5FkeP3n3BXxw&rqlang=cn&rsv_enter=1&rsv_sug3=1&rsv_sug2=0&inputT=611&rsv_sug4=611
        // 3.难以相应需求的变化，变化很没有幸福感
        // 4.编程体验不统一
        UserDto userDTO = this.userCenterFeignClient.findUserById(userId);

        ShareDTO shareDTO = new ShareDTO();
        // 属性的装配
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

    @Override
    public PageInfo<Share> query(String title, Integer pageNo, Integer pageSize, Integer userId) {
        //启动分页
        PageHelper.startPage(pageNo,pageSize);
        //构造查询实例
        Example example = new Example(Share.class);
        Example.Criteria criteria = example.createCriteria();
        //如标题关键字不变，则加上模糊查询条件，否则结果即所有数据
        if (StringUtil.isNotEmpty(title)){
            criteria.andLike("title","%" + title + "%");
        }
        //执行按条件查询
        List<Share> shares = this.shareMapper.selectByExample(example);
        //处理后的Share数据列表
        List<Share> sharesDeal;
        //如果用户未登录，downloadUrl全为null
        if (userId == null){
            sharesDeal = shares.stream()
                    .peek(share -> {
                        share.setDownloadUrl(null);
                    })
                    .collect(Collectors.toList());
        }
        //若登录，查询mid_user_share,没数据则url也为null
        else{
            sharesDeal = shares.stream()
                    .peek(share -> {
                        MidUserShare midUserShare = this.midUserShareMapper.selectOne(
                                 MidUserShare.builder()
                                .userId(userId)
                                .shareId(share.getId())
                                .build()
                        );
                        if (midUserShare == null){
                            share.setDownloadUrl(null);
                        }
                    })
                    .collect(Collectors.toList());
        }
        return new PageInfo<>(sharesDeal);
    }

    @Override
    public String getHello() {
        return this.userCenterFeignClient.getHello();
    }

    @Override
    public ResponseResult addContribute(ShareRequestDto shareRequestDto) {
        Share share = Share.builder().author(shareRequestDto.getAuthor())
              .showFlag(false).buyCount(0).cover("http://www.baidu.com")
              .isOriginal(shareRequestDto.getIsOriginal())
                .price(shareRequestDto.getPrice())
                .summary(shareRequestDto.getSummary())
                .title(shareRequestDto.getTitle())
                .createTime(Timestamp.valueOf(LocalDateTime.now()))
                .updateTime(Timestamp.valueOf(LocalDateTime.now()))
                .reason("")
                .userId(shareRequestDto.getUserId())
                .auditStatus("NOT_YET")
                .downloadUrl(shareRequestDto.getDownloadUrl())
                .build();
        int n = shareMapper.insert(share);
        if(n == 0) {
            System.out.println("投稿异常");
            return ResponseResult.failure(ResultCode.DATABASE_ERROR, "投稿异常");
        } else {
            System.out.println("投稿成功");
            return ResponseResult.success();
        }

    }

    @Override
    public List<Share> getShareInfoList(PageDto pageDto) {
        Example example = new Example(Share.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("showFlag", true);
        PageHelper.startPage(pageDto.getPageIndex(), pageDto.getPageSize(), "create_time desc");
        List<Share> shareList = shareMapper.selectByExample(example);
        System.out.println(shareList);
        //分页查询
        PageInfo<Share> pageInfo = new PageInfo<>(shareList);
        return pageInfo.getList();
    }

    @Override
    public ShareVo getShareVo(int id) {
        ShareVo shareVo = new ShareVo();
        Share share = shareMapper.selectByPrimaryKey(id);
        UserDto userDto = restTemplate.getForObject("http://47.111.64.183:8006/user/{id}", UserDto.class, id);
        /*UserDto userDto = userCenterFeignClient.getUserDtoById(share.getUserId());*/
        BeanUtils.copyProperties(share, shareVo);
        shareVo.setWxNickname(userDto.getWxNickname());
        return shareVo;
    }
    @Override
    public List<Share> getShareInfoByKeyWords(PageDto pageDto) {
        //启动分页
        PageHelper.startPage(pageDto.getPageIndex(), pageDto.getPageSize());
        //构造查询实例
        Example example = new Example(Share.class);
        Example.Criteria criteria = example.createCriteria();
        //如果标题不为空，再加上模糊查询条件，否则结果返回所有数据
        if (!StringUtil.isEmpty(pageDto.getKeywords())) {
            System.out.println("进入模糊查询条件赋值");
            criteria.andLike("title", "%" + pageDto.getKeywords() + "%");
        }
        System.out.println("获取的keywords是： " + pageDto.getKeywords());
        List<Share> shares = shareMapper.selectByExample(example);
        System.out.println("获取到的数据是: " + shares);
        PageInfo<Share> pageInfo = new PageInfo<>(shares);
        List<Share> shareDel;
        return pageInfo.getList();
    }

    @Override
    public ResponseResult insertShareInfo(ShareRequestDto shareRequestDto) {
        Share share = Share.builder().author(shareRequestDto.getAuthor())
                .showFlag(false).buyCount(0).cover("http://www.baidu.com")
                .isOriginal(shareRequestDto.getIsOriginal())
                .price(shareRequestDto.getPrice())
                .summary(shareRequestDto.getSummary())
                .title(shareRequestDto.getTitle())
                .createTime(Timestamp.valueOf(LocalDateTime.now()))
                .updateTime(Timestamp.valueOf(LocalDateTime.now()))
                .reason("")
                .userId(shareRequestDto.getUserId())
                .auditStatus("NOT_YET")
                .downloadUrl(shareRequestDto.getDownloadUrl())
                .build();
        int n = shareMapper.insert(share);
        if(n == 0) {
            System.out.println("投稿异常");
            return ResponseResult.failure(ResultCode.DATABASE_ERROR, "投稿异常");
        } else {
            System.out.println("投稿成功");
            return ResponseResult.success();
        }
    }

    @Override
    public Share audiById(Integer id, ShareAuditDTO shareAuditDTO) {
        //1. 查询share是否存在，不存在或者当前的audit_status != NOT_YET,那么抛异常
        long start = System.currentTimeMillis();
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法! 该分享不存在");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法! 该分享已审核通过或审核不通过！");
        }
        //2. 审核资源,将状态改为PASS或REJECT
        //这个API的主要流程是审核，所以不需要等更新积分的结果返回，可以将加积分改为异步
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        this.shareMapper.updateByPrimaryKey(share);
        //3. 如果是PASS，那么发送消息给rocketmq，让用户中心去消费，并为发布人添加积分
        if ("PASS".equals(shareAuditDTO.getAuditStatusEnum())) {
            System.out.println("进入发消息方法：");
            this.rocketMQTemplate.convertAndSend(
                    "hello-bonus",
                    UserAddBonusMsgDTO.builder()
                            .userId(share.getUserId())
                            .bonus(100)
                            .build()
            );
        }
        System.out.println("消耗的时间是： " + (System.currentTimeMillis() - start));
        return share;
    }

    @Override
    public Share audiById1(Integer id, ShareAuditDTO shareAuditDTO) {
        //1. 查询share是否存在，不存在或者当前的audit_status != NOT_YET,那么抛异常
        long start = System.currentTimeMillis();
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法! 该分享不存在");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法! 该分享已审核通过或审核不通过！");
        }
        //2. 审核资源,将状态改为PASS或REJECT
        //这个API的主要流程是审核，所以不需要等更新积分的结果返回，可以将加积分改为异步
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        this.shareMapper.updateByPrimaryKey(share);
        System.out.println("用户id是>>>>" + share.getUserId());
        System.out.println("是否通过: " + AuditStatusEnum.PASS.equals(shareAuditDTO.getAuditStatusEnum()));
        //3. 如果是PASS，那么发送消息给rocketmq，让用户中心去消费，并为发布人添加积分
        if ("PASS".equals(shareAuditDTO.getAuditStatusEnum())) {
            System.out.println("进入发消息方法：");
            //feign的调用
            userCenterFeignClient.updateUserBonusByUserId(UserAddBonusMsgDTO.builder().userId(share.getUserId()).bonus(100).build());
        }
        System.out.println("消耗的时间是： " + (System.currentTimeMillis() - start) + "ms");
        return share;
    }

    @Override
    public Share audiByAsyncRestTemplate(Integer id, ShareAuditDTO shareAuditDTO) {
        String url = "http://localhost:8006/user/update/bonus1";
        //设置Header
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        //1. 查询share是否存在，不存在或者当前的audit_status != NOT_YET,那么抛异常
        long start = System.currentTimeMillis();
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法! 该分享不存在");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法! 该分享已审核通过或审核不通过！");
        }
        //2. 审核资源,将状态改为PASS或REJECT
        //这个API的主要流程是审核，所以不需要等更新积分的结果返回，可以将加积分改为异步
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        this.shareMapper.updateByPrimaryKey(share);
        //3. 如果是PASS，那么发送消息给rocketmq，让用户中心去消费，并为发布人添加积分
        if ("PASS".equals(shareAuditDTO.getAuditStatusEnum())) {
            System.out.println("AsyncRestTemplate异步发送消息：");
            UserAddBonusMsgDTO userAddBonusMsgDTO = UserAddBonusMsgDTO.builder().userId(share.getUserId()).bonus(100).build();
            HttpEntity<Object> httpEntity = new HttpEntity<>(userAddBonusMsgDTO ,headers);
            //异步发送
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(userAddBonusMsgDTO);
            ListenableFuture<ResponseEntity<Integer>> addBounds = asyncRestTemplate.postForEntity(url, httpEntity, Integer.class);
            addBounds.addCallback(result -> log.info(result.getBody().toString()),(e) -> log.error(e.getMessage()));
        }
        System.out.println("消耗的时间是： " + (System.currentTimeMillis() - start) + "ms");
        return share;
    }

    @Override
    public Share audiByAsync(Integer id, ShareAuditDTO shareAuditDTO) {
        String url = "http://localhost:8006/user/update/bonus";
        //1. 查询share是否存在，不存在或者当前的audit_status != NOT_YET,那么抛异常
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法! 该分享不存在");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法! 该分享已审核通过或审核不通过！");
        }
        //2. 审核资源,将状态改为PASS或REJECT
        //这个API的主要流程是审核，所以不需要等更新积分的结果返回，可以将加积分改为异步
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        this.shareMapper.updateByPrimaryKey(share);
        System.out.println("现在的时间是i: " + LocalDateTime.now());
        //3. 如果是PASS，那么发送消息给rocketmq，让用户中心去消费，并为发布人添加积分
        if ("PASS".equals(shareAuditDTO.getAuditStatusEnum())) {
            System.out.println("AsyncRestTemplate异步发送消息：");
            auditByAsync1(share.getUserId());
        }
        return share;
    }

    @Async
    public void auditByAsync1(int userId) {
        System.out.println("现在的时间是： " + LocalDateTime.now());
        userCenterFeignClient.updateUserBonusByUserId(UserAddBonusMsgDTO.builder().userId(userId).bonus(100).build());
    }
}
