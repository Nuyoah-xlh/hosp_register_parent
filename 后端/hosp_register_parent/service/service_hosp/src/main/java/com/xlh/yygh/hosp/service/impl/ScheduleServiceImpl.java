package com.xlh.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xlh.yygh.common.exception.YyghException;
import com.xlh.yygh.common.result.ResultCodeEnum;
import com.xlh.yygh.hosp.config.HospConfig;
import com.xlh.yygh.hosp.repository.ScheduleRepository;
import com.xlh.yygh.hosp.service.DepartmentService;
import com.xlh.yygh.hosp.service.HospitalService;
import com.xlh.yygh.hosp.service.ScheduleService;
import com.xlh.yygh.model.hosp.BookingRule;
import com.xlh.yygh.model.hosp.Department;
import com.xlh.yygh.model.hosp.Hospital;
import com.xlh.yygh.model.hosp.Schedule;
import com.xlh.yygh.vo.hosp.BookingScheduleRuleVo;
import com.xlh.yygh.vo.hosp.ScheduleOrderVo;
import com.xlh.yygh.vo.hosp.ScheduleQueryVo;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.logging.log4j.message.ReusableMessage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public void save(Map<String, Object> paramMap) {
        //????????????
        String mapString = JSONObject.toJSONString(paramMap);
        Schedule schedule1 = JSONObject.parseObject(mapString, Schedule.class);
        //???????????????????????????
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule1.getHoscode(), schedule1.getHosScheduleId());
        //???????????????
        if (schedule != null) {
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
        //??????????????????
        else {
            Date date = new Date();
            schedule1.setStatus(1);
            schedule1.setCreateTime(date);
            schedule1.setUpdateTime(date);
            schedule1.setIsDeleted(0);
            scheduleRepository.save(schedule1);
        }
    }

    @Override
    public Page<Schedule> findDepartmentsByPage(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        //????????????????????????????????????????????????0????????????
        Pageable pageable = PageRequest.of(page - 1, limit);
        //??????Example??????
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        //??????????????????
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setStatus(1);
        schedule.setIsDeleted(0);
        Example<Schedule> example = Example.of(schedule, exampleMatcher);
        //??????
        return scheduleRepository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String scheduleId) {
        //??????????????????????????????
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, scheduleId);
        if (schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        //1 ?????????????????? ??? ???????????? ??????
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        //2 ???????????????workDate???????????????
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),//????????????
                Aggregation.group("workDate")//????????????
                        .first("workDate").as("workDate")
                        //3 ??????????????????
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //??????
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
                //??????
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        //??????????????????
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggregate.getMappedResults();

        //???????????????????????????
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAgg,
                        Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResults.getMappedResults().size();

        //???????????????????????????
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }

        //?????????????????????????????????
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList", bookingScheduleRuleVoList);
        result.put("total", total);

        //??????????????????
        String hosName = hospitalService.getHospitalByHoscode(hoscode).getHosname();
        //??????????????????
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hosName);
        result.put("baseMap", baseMap);
        return result;
    }

    /**
     * ??????????????????????????????
     *
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "??????";
            default:
                break;
        }
        return dayOfWeek;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        //??????????????????mongodb
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        //?????????list????????????????????????????????????????????????????????????????????????????????????
        scheduleList.stream().forEach(item -> {
            this.packageSchedule(item);
        });
        return scheduleList;
    }


    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        Map<String, Object> result = new HashMap<>();

        //??????????????????
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        if(null == hospital) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();

        //?????????????????????????????????
        IPage iPage = this.getListDate(page, limit, bookingRule);
        //????????????????????????
        List<Date> dateList = iPage.getRecords();
        //??????????????????????????????????????????
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(dateList);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")//????????????
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregationResults.getMappedResults();
        //???????????????????????????
        //???????????? ???????????????ScheduleVo?????????????????????????????????BookingRuleVo
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(scheduleVoList)) {
            scheduleVoMap = scheduleVoList.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }
        //???????????????????????????
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for(int i=0, len=dateList.size(); i<len; i++) {
            Date date = dateList.get(i);

            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            if(null == bookingScheduleRuleVo) { // ??????????????????????????????
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //??????????????????
                bookingScheduleRuleVo.setDocCount(0);
                //?????????????????????  -1????????????
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            //?????????????????????????????????
            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //?????????????????????????????????????????????   ?????? 0????????? 1??????????????? -1????????????????????????
            if(i == len-1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //??????????????????????????????????????? ????????????
            if(i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if(stopTime.isBeforeNow()) {
                    //????????????
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        //???????????????????????????
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());
        //??????????????????
        Map<String, String> baseMap = new HashMap<>();
        //????????????
        baseMap.put("hosname", hospitalService.getHospitalByHoscode(hoscode).getHosname());
        //??????
        Department department =departmentService.getDepartment(hoscode, depcode);
        //???????????????
        baseMap.put("bigname", department.getBigname());
        //????????????
        baseMap.put("depname", department.getDepname());
        //???
        baseMap.put("workDateString", new DateTime().toString("yyyy???MM???"));
        //????????????
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //????????????
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;

    }

    //?????????????????????????????????
    private IPage getListDate(Integer page, Integer limit, BookingRule bookingRule) {
        //??????????????????
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        //????????????
        int cycle = bookingRule.getCycle();
        //??????????????????????????????????????????????????????????????????????????????????????????1
        if(releaseTime.isBeforeNow()) cycle += 1;
        //???????????????????????????????????????????????????????????????
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            //????????????????????????
            DateTime curDateTime = new DateTime().plusDays(i);
            String dateString = curDateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(dateString).toDate());
        }
        //?????????????????????????????????????????????????????????????????????7????????????????????????????????????
        List<Date> pageDateList = new ArrayList<>();
        int start = (page-1)*limit;
        int end = (page-1)*limit+limit;
        if(end >dateList.size()) end = dateList.size();
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page(page, 7, dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;

    }

    /**
     * ???Date?????????yyyy-MM-dd HH:mm????????????DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    @Override
    public Schedule getById(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        return this.packageSchedule(schedule);
    }

    //??????????????????????????? ????????????????????????????????????????????????
    private Schedule packageSchedule(Schedule schedule) {
        //??????????????????
        schedule.getParam().put("hosname",hospitalService.getHospitalByHoscode(schedule.getHoscode()).getHosname());
        //??????????????????
        schedule.getParam().put("depname",departmentService.getDepName(schedule.getHoscode(),schedule.getDepcode()));
        //????????????????????????
        schedule.getParam().put("dayOfWeek",this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
        return schedule;
    }

    //????????????id????????????????????????
    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        //??????????????????
        Schedule schedule = this.getById(scheduleId);
        //Schedule schedule = baseMapper.selectById(scheduleId);
        if(schedule == null) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //????????????????????????
        Hospital hospital = hospitalService.getHospitalByHoscode(schedule.getHoscode());
        if(hospital == null) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        if(bookingRule == null) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //????????????????????????scheduleOrderVo
        scheduleOrderVo.setHoscode(schedule.getHoscode());
        scheduleOrderVo.setHosname(hospitalService.getHospitalByHoscode(schedule.getHoscode()).getHosname());
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        scheduleOrderVo.setDepname(departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());

        //?????????????????????????????????????????????-1????????????0???
        int quitDay = bookingRule.getQuitDay();
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());

        //??????????????????
        DateTime startTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());
        System.out.println("????????????");
        System.out.println(new Date());
        System.out.println(bookingRule.getReleaseTime());
        System.out.println(scheduleOrderVo.getStartTime());

        //??????????????????
        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        //????????????????????????
        DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStopTime(stopTime.toDate());
        return scheduleOrderVo;
    }

    @Override
    public void update(Schedule schedule) {
        schedule.setUpdateTime(new Date());
        //????????????????????????
        scheduleRepository.save(schedule);
    }

}
