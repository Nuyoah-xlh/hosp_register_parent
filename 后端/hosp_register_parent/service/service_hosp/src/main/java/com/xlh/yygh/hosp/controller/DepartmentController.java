package com.xlh.yygh.hosp.controller;

import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.hosp.service.DepartmentService;
import com.xlh.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //根据医院编号，查询医院的所有科室
    @ApiOperation("查询医院的所有科室")
    @GetMapping("/getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode){
        List<DepartmentVo> departmentVoList= departmentService.findDeptTree(hoscode);
        return Result.ok(departmentVoList);
    }


}
