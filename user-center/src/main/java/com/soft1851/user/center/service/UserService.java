package com.soft1851.user.center.service;

import com.soft1851.user.center.domain.dto.LoginDto;
import com.soft1851.user.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.user.center.domain.dto.UserDto;

import com.soft1851.user.center.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    User findById(Integer id);

    public List<Map<String, Object>> getTeacher();

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    UserDto getUserById(int id);

    /**
     * 登录接口
     * @param loginDto
     * @return
     */
    User login(LoginDto loginDto);

    /**
     * 修改积分
     * @return
     */
    int updateBonus(UserAddBonusMsgDTO userAddBonusMsgDTO);
}
