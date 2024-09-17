package com.nfu.twohand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nfu.twohand.pojo.Comment;
import com.nfu.twohand.service.CommentService;
import com.nfu.twohand.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author WH
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2024-09-06 19:04:37
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




