package com.nfu.twohand.pojo;

import lombok.Data;

@Data
public class CountNumber {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 订单数量
     */
    private String count;
}
