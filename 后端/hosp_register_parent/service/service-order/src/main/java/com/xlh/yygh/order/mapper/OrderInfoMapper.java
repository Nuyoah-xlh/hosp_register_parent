package com.xlh.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Ordering;
import com.xlh.yygh.model.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

}
