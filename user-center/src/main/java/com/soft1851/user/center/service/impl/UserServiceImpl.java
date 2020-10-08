package com.soft1851.user.center.service.impl;

import com.soft1851.user.center.domain.dto.LoginDto;
import com.soft1851.user.center.domain.dto.UserAddBonusMsgDTO;
import com.soft1851.user.center.domain.dto.UserDto;
import com.soft1851.user.center.domain.entity.User;
import com.soft1851.user.center.mapper.UserMapper;
import com.soft1851.user.center.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RestTemplate restTemplate;

//    @Override
//    public List<Map<String, Object>> getShareList() {
//        List<Map<String, Object>> userList = null;
//        List<Share> users = userMapper.selectAll();
//        for(int i = 0; i < users.size(); i++) {
//            //Share share = shares.get(i);
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("share", users);
//            userList.add(map);
//        }
//        return userList;
//    }

    @Override
    public User findById(Integer id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Map<String, Object>> getTeacher() {
//        List<Map<String, Object>> shareList = null;
//        List<Share> users = userMapper.selectByPrimaryKey(id);
//        for(int i = 0; i < users.size(); i++) {
//            //Share share = shares.get(i);
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("share", users);
//            shareList.add(map);
//        }
        return null;
    }

    @Override
    public UserDto getUserById(int id) {
        System.out.println("业务逻辑层中的接口：" + id);
        User user = userMapper.selectByPrimaryKey(id);
        System.out.println("获取到的数据是>>>>>>>" + user);
        return UserDto.builder().wxNickname(user.getWxNickname()).build();
    }

    @Override
    public User login(LoginDto loginDto) {
        Example example = new Example(User.class);
        example.setOrderByClause("create_time DESC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("account", loginDto.getAccount());
        User user = userMapper.selectOneByExample(example);
        if(user.getPassword().equals(loginDto.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public int updateBonus(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        User user = userMapper.selectByPrimaryKey(userAddBonusMsgDTO.getUserId());
        System.out.println("修改用户积分：" + userAddBonusMsgDTO.getBounds());
        user.setBonus(user.getBonus() + userAddBonusMsgDTO.getBounds());
        return userMapper.updateByPrimaryKeySelective(user);
    }

}
