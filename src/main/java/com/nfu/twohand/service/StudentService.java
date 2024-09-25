package com.nfu.twohand.service;

import com.nfu.twohand.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WH
* @description 针对表【student】的数据库操作Service
* @createDate 2024-09-06 19:04:37
*/
public interface StudentService extends IService<Student> {

    boolean login(String username, String password);

    // 生成验证码的方法（6位数字）
    String generateVerificationCode();
}
