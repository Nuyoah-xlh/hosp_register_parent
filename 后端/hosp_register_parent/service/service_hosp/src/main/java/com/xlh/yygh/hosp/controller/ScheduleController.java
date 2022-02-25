package com.xlh.yygh.hosp.controller;

import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.hosp.service.ScheduleService;
import com.xlh.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
//@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //根据医院编号、科室编号，查询排班规则数据
    @ApiOperation("查询排班规则数据")
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable Integer page,@PathVariable Integer limit,@PathVariable String hoscode,@PathVariable String depcode){
        Map<String,Object> map= scheduleService.getScheduleRule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    //根据医院编号、科室编号、工作日期，查询排班详细信息
    @ApiOperation("根据医院编号、科室编号、工作日期，查询排班详细信息")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,@PathVariable String depcode,@PathVariable String workDate){
        System.out.println(hoscode+"  "+depcode+"   "+workDate);
        List<Schedule> list=scheduleService.getScheduleDetail(hoscode,depcode,workDate);
        return Result.ok(list);
    }


}
