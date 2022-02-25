package com.xlh.yygh.hosp.controller.api;

import com.xlh.yygh.common.exception.YyghException;
import com.xlh.yygh.common.helper.HttpRequestHelper;
import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.common.utils.MD5;
import com.xlh.yygh.hosp.service.DepartmentService;
import com.xlh.yygh.hosp.service.HospitalService;
import com.xlh.yygh.hosp.service.HospitalSetService;
import com.xlh.yygh.hosp.service.ScheduleService;
import com.xlh.yygh.model.hosp.Department;
import com.xlh.yygh.model.hosp.Hospital;
import com.xlh.yygh.model.hosp.Schedule;
import com.xlh.yygh.vo.hosp.DepartmentQueryVo;
import com.xlh.yygh.vo.hosp.ScheduleQueryVo;
import javafx.geometry.Pos;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.query.PartTreeMongoQuery;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RelationSupport;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.sql.ResultSet;
import java.util.Map;

import static com.xlh.yygh.common.result.ResultCodeEnum.SIGN_ERROR;

//由医院接口模拟系统调用
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //显示医院详细信息
    @PostMapping("/hospital/show")
    public Result show(HttpServletRequest httpServletRequest){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap=httpServletRequest.getParameterMap();
        Map<String, Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //获取传递过来的签名
        String hospSign=(String)paramMap.get("sign");
        //根据医院code查询数据库中的签名
        String hoscode=(String)paramMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);
        //把数据库查询的签名加密
        String signKeyMd5= MD5.encrypt(signKey);
        //如果签名不一致，则抛出异常
        if(signKeyMd5.isEmpty()||!signKeyMd5.equals(hospSign)){
            throw new YyghException(SIGN_ERROR);
        }

        //service查询
        Hospital hospital=hospitalService.getHospitalByHoscode(hoscode);
        if(hospital!=null){
            return Result.ok(hospital);
        }
        else{
            return Result.fail();
        }
    }

    //上传/修改医院信息
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest httpServletRequest){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap=httpServletRequest.getParameterMap();
        Map<String, Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //获取传递过来的签名
        String hospSign=(String)paramMap.get("sign");
        //根据医院code查询数据库中的签名
        String hoscode=(String)paramMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);
        //把数据库查询的签名加密
        String signKeyMd5= MD5.encrypt(signKey);
        //如果签名不一致，则抛出异常
        if(signKeyMd5.isEmpty()||!signKeyMd5.equals(hospSign)){
            throw new YyghException(SIGN_ERROR);
        }
        //图片转为base64字串时，会将“+”转为了“ ”，这里需要转回来
        String logoData=(String)paramMap.get("logoData");
        logoData=logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);
        //调用service方法
        hospitalService.save(paramMap);
        return Result.ok();
    }

    //上传/修改科室信息
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest httpServletRequest){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap=httpServletRequest.getParameterMap();
        Map<String, Object> paramMap= HttpRequestHelper.switchMap(requestMap);
        //获取传递过来的签名
        String hospSign=(String)paramMap.get("sign");
        //根据医院code查询数据库中的签名
        String hoscode=(String)paramMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);
        //把数据库查询的签名加密
        String signKeyMd5= MD5.encrypt(signKey);
        //如果签名不一致，则抛出异常
        if(signKeyMd5.isEmpty()||!signKeyMd5.equals(hospSign)){
            throw new YyghException(SIGN_ERROR);
        }
        //调用service方法实现功能
        departmentService.save(paramMap);
        return Result.ok();
    }

    //查询所有科室
    @PostMapping("/department/list")
    public Result findDepartments(HttpServletRequest httpServletRequest){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap=httpServletRequest.getParameterMap();
        Map<String, Object> paramMap= HttpRequestHelper.switchMap(requestMap);

        //获取传递过来的签名
        String hospSign=(String)paramMap.get("sign");
        //根据医院code查询数据库中的签名
        String hoscode=(String)paramMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);
        //把数据库查询的签名加密
        String signKeyMd5= MD5.encrypt(signKey);
        //如果签名不一致，则抛出异常
        if(signKeyMd5.isEmpty()||!signKeyMd5.equals(hospSign)){
            throw new YyghException(SIGN_ERROR);
        }

        //当前页和每页记录数
        int page=StringUtils.isEmpty(paramMap.get("page"))?1:Integer.parseInt((String) paramMap.get("page"));
        int limit=StringUtils.isEmpty(paramMap.get("limit"))?1:Integer.parseInt((String) paramMap.get("limit"));
        DepartmentQueryVo departmentQueryVo=new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        //调用service方法
        Page<Department> departmentPage= departmentService.findDepartmentsByPage(page,limit,departmentQueryVo);
        return Result.ok(departmentPage);
    }

    //删除科室
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest httpServletRequest){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap=httpServletRequest.getParameterMap();
        Map<String, Object> paramMap= HttpRequestHelper.switchMap(requestMap);

        //获取传递过来的签名
        String hospSign=(String)paramMap.get("sign");
        //根据医院code查询数据库中的签名
        String hoscode=(String)paramMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);
        //把数据库查询的签名加密
        String signKeyMd5= MD5.encrypt(signKey);
        //如果签名不一致，则抛出异常
        if(signKeyMd5.isEmpty()||!signKeyMd5.equals(hospSign)){
            throw new YyghException(SIGN_ERROR);
        }
        String depCode=(String)paramMap.get("depcode");
        departmentService.removeDept(hoscode,depCode);
        return Result.ok();
    }

    //上传排班
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest httpServletRequest){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap=httpServletRequest.getParameterMap();
        Map<String, Object> paramMap= HttpRequestHelper.switchMap(requestMap);

        //获取传递过来的签名
        String hospSign=(String)paramMap.get("sign");
        //根据医院code查询数据库中的签名
        String hoscode=(String)paramMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);
        //把数据库查询的签名加密
        String signKeyMd5= MD5.encrypt(signKey);
        //如果签名不一致，则抛出异常
        if(signKeyMd5.isEmpty()||!signKeyMd5.equals(hospSign)){
            throw new YyghException(SIGN_ERROR);
        }
        scheduleService.save(paramMap);
        return Result.ok();
    }

    //查询排班
    @PostMapping("/schedule/list")
    public Result findSchedules(HttpServletRequest httpServletRequest){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap=httpServletRequest.getParameterMap();
        Map<String, Object> paramMap= HttpRequestHelper.switchMap(requestMap);

        //获取传递过来的签名
        String hospSign=(String)paramMap.get("sign");
        //根据医院code查询数据库中的签名
        String hoscode=(String)paramMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);
        //把数据库查询的签名加密
        String signKeyMd5= MD5.encrypt(signKey);
        //如果签名不一致，则抛出异常
        if(signKeyMd5.isEmpty()||!signKeyMd5.equals(hospSign)){
            throw new YyghException(SIGN_ERROR);
        }

        //科室编号
        String depCode=(String)paramMap.get("depcode");

        //当前页和每页记录数
        int page=StringUtils.isEmpty(paramMap.get("page"))?1:Integer.parseInt((String) paramMap.get("page"));
        int limit=StringUtils.isEmpty(paramMap.get("limit"))?1:Integer.parseInt((String) paramMap.get("limit"));
        ScheduleQueryVo scheduleQueryVo=new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depCode);
        //调用service方法
        Page<Schedule> schedulePage= scheduleService.findDepartmentsByPage(page,limit,scheduleQueryVo);
        return Result.ok(schedulePage);
    }

    //删除排班
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest httpServletRequest){
        //获取传递过来的医院信息
        Map<String, String[]> requestMap=httpServletRequest.getParameterMap();
        Map<String, Object> paramMap= HttpRequestHelper.switchMap(requestMap);

        //获取传递过来的签名
        String hospSign=(String)paramMap.get("sign");
        //根据医院code查询数据库中的签名
        String hoscode=(String)paramMap.get("hoscode");
        String signKey=hospitalSetService.getSignKey(hoscode);
        //把数据库查询的签名加密
        String signKeyMd5= MD5.encrypt(signKey);
        //如果签名不一致，则抛出异常
        if(signKeyMd5.isEmpty()||!signKeyMd5.equals(hospSign)){
            throw new YyghException(SIGN_ERROR);
        }
        String scheduleId=(String) paramMap.get("hosScheduleId");
        scheduleService.remove(hoscode,scheduleId);
        return Result.ok();
    }

}
