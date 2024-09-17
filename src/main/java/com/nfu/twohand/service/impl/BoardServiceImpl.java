package com.nfu.twohand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nfu.twohand.pojo.Board;
import com.nfu.twohand.service.BoardService;
import com.nfu.twohand.mapper.BoardMapper;
import org.springframework.stereotype.Service;

/**
* @author WH
* @description 针对表【board】的数据库操作Service实现
* @createDate 2024-09-06 19:04:37
*/
@Service
public class BoardServiceImpl extends ServiceImpl<BoardMapper, Board>
    implements BoardService{

}




