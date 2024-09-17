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
 * @TableName g_order
 */
@TableName(value ="g_order")
@Data
public class GOrder implements Serializable {
    /**
     * 订单编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 顾客id
     */
    private Integer sid;

    /**
     * 菜品id
     */
    private Integer gid;

    /**
     * 下单日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date order_time;

    /**
     * 订单总额
     */
    private Double total;

    /**
     * 订单数量
     */
    private Integer count;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 是否支付
     */
    private Integer isorder;

    @TableField(exist = false)
    private Good good;

    @TableField(exist = false)
    private Student student;

    @TableField(exist = false)
    private double totalMoney;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}