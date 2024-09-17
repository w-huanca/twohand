package com.nfu.twohand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nfu.twohand.pojo.CountNumber;
import com.nfu.twohand.pojo.GOrder;
import com.nfu.twohand.service.GOrderService;
import com.nfu.twohand.mapper.GOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author WH
* @description 针对表【g_order】的数据库操作Service实现
* @createDate 2024-09-06 19:04:37
*/
@Service
public class GOrderServiceImpl extends ServiceImpl<GOrderMapper, GOrder>
    implements GOrderService{

}




