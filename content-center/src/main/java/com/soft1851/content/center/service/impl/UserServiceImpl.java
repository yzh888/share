package com.soft1851.content.center.service.impl;

import com.soft1851.content.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.content.center.domain.entity.User;
import com.soft1851.content.center.feignclient.UserCenterFeignClient;
import com.soft1851.content.center.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserCenterFeignClient userCenterFeignClient;

    @Override
    public User updateBonus(UserAddBonusMsgDTO updateBonus) {
        return userCenterFeignClient.updateBonus(updateBonus);
    }
}
