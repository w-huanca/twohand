package com.nfu.twohand.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName score_good
 */
@TableName(value ="score_good")
@Data
public class ScoreGood implements Serializable {
    /**
     * 
     */
    private Integer userId;

    /**
     * 
     */
    private Integer goodId;

    /**
     * 
     */
    private Integer score;

    /**
     * 
     */
    private Date time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}