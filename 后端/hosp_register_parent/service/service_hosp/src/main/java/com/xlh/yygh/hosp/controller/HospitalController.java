package com.xlh.yygh.hosp.controller;

import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.hosp.service.HospitalService;
import com.xlh.yygh.model.hosp.Hospital;
import com.xlh.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/hospital")
//@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //医院列表分页
    @ApiOperation("医院列表分页")
    @GetMapping("/list/{page}/{limit}")
    public Result listHosp(@PathVariable int page, @PathVariable int limit, HospitalQueryVo hospitalQueryVo){
        Page<Hospital> page1=hospitalService.selectHospPage(page-1,limit,hospitalQueryVo);
        return  Result.ok(page1);
    }

    //更新医院上线状态
    @ApiOperation("更新医院上线状态")
    @GetMapping("/updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id,@PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    //获取医院详细信息
    @ApiOperation("获取医院详细信息")
    @GetMapping("/showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        Map<String,Object> map=hospitalService.getHospById(id);
        return Result.ok(map);
    }
}
