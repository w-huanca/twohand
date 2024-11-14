package com.nfu.twohand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nfu.twohand.pojo.Good;
import com.nfu.twohand.service.GoodService;
import com.nfu.twohand.mapper.GoodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WH
 * @description 针对表【good】的数据库操作Service实现
 * @createDate 2024-09-06 19:04:37
 */
@Service
public class GoodServiceImpl extends ServiceImpl<GoodMapper, Good>
        implements GoodService {

    @Autowired
    private GoodMapper goodMapper;

    @Override
    public Page<Good> getGoodsByPage(Page<Good> page, QueryWrapper<Good> qw) {
        Page<Good> goods = goodMapper.selectPage(page, qw);
        System.out.println("数据为：" + goods.getRecords());
        System.out.println("总数为：" + goods.getTotal() + ",总页数为：" + goods.getPages());
        System.out.println("当前页为：" + goods.getCurrent() + ",每页限制：" + goods.getSize());
        return goods;
    }
}




