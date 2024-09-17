package com.nfu.twohand.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName type
 */
@TableName(value ="type")
@Data
public class Type implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 类型名称
     */
    private String type;

    /**
     * 类型特征
     */
    private String feature;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}