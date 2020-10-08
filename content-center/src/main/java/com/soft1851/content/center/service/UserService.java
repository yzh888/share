package com.soft1851.content.center.service;

import com.soft1851.content.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.content.center.domain.entity.User;

public interface UserService {
    User updateBonus(UserAddBonusMsgDTO updateBonus);
}
