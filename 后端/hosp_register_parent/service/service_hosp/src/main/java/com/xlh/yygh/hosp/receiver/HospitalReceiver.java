package com.xlh.yygh.hosp.receiver;

import com.rabbitmq.client.Channel;
import com.xlh.common.rabbit.constant.MqConst;
import com.xlh.common.rabbit.service.RabbitService;
import com.xlh.yygh.hosp.service.ScheduleService;
import com.xlh.yygh.model.hosp.Schedule;
import com.xlh.yygh.vo.msm.MsmVo;
import com.xlh.yygh.vo.order.OrderMqVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HospitalReceiver {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        //下单成功更新预约数
        Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
        schedule.setReservedNumber(orderMqVo.getReservedNumber());
        schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        scheduleService.update(schedule);
        //发送短信
        //MsmVo msmVo = orderMqVo.getMsmVo();
        //if(null != msmVo) {
        //    rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        //}
    }
}
