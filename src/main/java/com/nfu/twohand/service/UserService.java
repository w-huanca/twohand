package com.nfu.twohand.service;

import com.nfu.twohand.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WH
* @description 针对表【user】的数据库操作Service
* @createDate 2024-09-06 19:04:37
*/
public interface UserService extends IService<User> {

    boolean login(String username, String password);
}
