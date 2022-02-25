package com.xlh.yygh.hosp.service;

import com.xlh.yygh.model.hosp.Schedule;
import com.xlh.yygh.vo.hosp.ScheduleOrderVo;
import com.xlh.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    //上传排班
    void save(Map<String, Object> paramMap);

    //查询排班
    Page<Schedule> findDepartmentsByPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    //删除排班
    void remove(String hoscode, String scheduleId);

    //根据医院编号、科室编号，查询排班规则数据
    Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    //根据医院编号、科室编号、工作日期，查询排班详细信息
    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    Schedule getById(String scheduleId);

    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    /**
     * 修改排班数据
     */
    void update(Schedule schedule);


}
