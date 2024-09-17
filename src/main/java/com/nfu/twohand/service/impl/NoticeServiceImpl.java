package com.nfu.twohand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nfu.twohand.pojo.Notice;
import com.nfu.twohand.service.NoticeService;
import com.nfu.twohand.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author WH
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2024-09-06 19:04:37
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

}




