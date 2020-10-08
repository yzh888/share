package com.soft1851.content.center.service;

import com.soft1851.content.center.domain.entity.Notice;

public interface NoticeService {
    /**
     * 查询最新公告
     * @return
     */
    Notice getLatest();
}
