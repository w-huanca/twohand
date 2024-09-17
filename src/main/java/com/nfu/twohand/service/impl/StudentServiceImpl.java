package com.nfu.twohand.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nfu.twohand.pojo.Student;
import com.nfu.twohand.service.StudentService;
import com.nfu.twohand.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author WH
* @description 针对表【student】的数据库操作Service实现
* @createDate 2024-09-06 19:04:37
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public boolean login(String username, String password) {

        Student student = studentMapper.selectOne(new QueryWrapper<Student>()
                .eq("sname", username));
        if (student == null) {
            return false;
        }

        String pwd = DigestUtil.md5Hex(password);
        return Objects.equals(pwd, student.getPassword());
    }
}




