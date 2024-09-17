package com.nfu.twohand.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName good
 */
@TableName(value ="good")
@Data
public class Good implements Serializable {
    /**
     * 菜品id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 菜品名称
     */
    private String gname;

    /**
     * 所属店铺
     */
    private String type;

    /**
     * 单价
     */
    private Double price;

    /**
     * 
     */
    private Double pricen;

    /**
     * 容量
     */
    private Integer stock;

    /**
     * 简介
     */
    private String descr;

    /**
     * 图像
     */
    private String gimage;

    /**
     * 
     */
    private Integer sid;

    /**
     * 
     */
    private Integer star;

    /**
     * 
     */
    private Double discount;

    /**
     * 用于关联学生信息
     */
    @TableField(exist = false)
    private Student student;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}