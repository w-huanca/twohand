package com.nfu.twohand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nfu.twohand.pojo.Type;
import com.nfu.twohand.service.TypeService;
import com.nfu.twohand.mapper.TypeMapper;
import org.springframework.stereotype.Service;

/**
* @author WH
* @description 针对表【type】的数据库操作Service实现
* @createDate 2024-09-06 19:04:37
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

}




