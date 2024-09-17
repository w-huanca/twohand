package com.nfu.twohand.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName student
 */
@TableName(value ="student")
@Data
public class Student implements Serializable {
    /**
     * 学生学号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 学生姓名
     */
    private String sname;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 所属专业
     */
    private String major;

    /**
     * 所属学院
     */
    private String college;

    /**
     * 
     */
    private String simage;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String address;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}