package com.xlh.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xlh.yygh.model.order.OrderInfo;
import com.xlh.yygh.vo.order.OrderQueryVo;

public interface OrderService extends IService<OrderInfo> {
    //创建订单
    Object saveOrder(String scheduleId, Long patientId);

    //查询订单分页
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    OrderInfo getOrder(Long orderId);

}
