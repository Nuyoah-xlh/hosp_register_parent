package com.xlh.yygh.order.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.common.utils.AuthContextHolder;
import com.xlh.yygh.enums.OrderStatusEnum;
import com.xlh.yygh.model.order.OrderInfo;
import com.xlh.yygh.order.service.OrderService;
import com.xlh.yygh.vo.order.OrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "创建订单")
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public Result submitOrder(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId,
            @ApiParam(name = "patientId", value = "就诊人id", required = true)
            @PathVariable Long patientId) {
        return Result.ok(orderService.saveOrder(scheduleId, patientId));
    }

    //订单列表（条件查询带分页）
    @GetMapping("/auth/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        IPage<OrderInfo> pageModel =
                orderService.selectPage(pageParam,orderQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping("/auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    //根据订单id查询订单详情
    @GetMapping("/auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable Long orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }

    //修改订单状态
    @GetMapping("/auth/setStatus/{orderId}/{status}")
    public Result getOrders(@PathVariable Long orderId,@PathVariable Integer status) {
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setOrderStatus(status);
        orderService.save(orderInfo);
        return Result.ok();
    }


}
