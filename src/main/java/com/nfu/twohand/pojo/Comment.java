package com.nfu.twohand.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * 评论id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 
     */
    private Integer goodId;

    /**
     * 评论人
     */
    private String customer;

    /**
     * 
     */
    private String cimage;

    /**
     * 
     */
    private String comment;

    /**
     * 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentTime;

    /**
     * 
     */
    private Integer star;

    /**
     * 关联查询商品
     */
    @TableField(exist = false)
    private Good good;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}