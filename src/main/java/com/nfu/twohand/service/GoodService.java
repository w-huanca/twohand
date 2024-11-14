package com.nfu.twohand.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nfu.twohand.pojo.Good;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WH
* @description 针对表【good】的数据库操作Service
* @createDate 2024-09-06 19:04:37
*/
public interface GoodService extends IService<Good> {

    Page<Good> getGoodsByPage(Page<Good> page, QueryWrapper<Good> queryWrapper);
}
