package com.nfu.twohand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nfu.twohand.pojo.User;
import com.nfu.twohand.service.UserService;
import com.nfu.twohand.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.Objects;

/**
* @author WH
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-09-06 19:04:37
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean login(String username, String password) {

        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("username", username));
        if (user == null) {
            return false;
        }

        String pwd = DigestUtil.md5Hex(password);
        return Objects.equals(pwd, user.getPassword());
    }
}




