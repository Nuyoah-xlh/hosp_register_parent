package com.xlh.yygh.cmn.controller;

import com.xlh.yygh.cmn.service.DictService;
import com.xlh.yygh.cmn.service.impl.DictServiceImpl;
import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;


    //导出数据字典
    @ApiOperation(value = "导出数据字典")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse httpServletResponse){
        dictService.exportDictData(httpServletResponse);
    }

    @ApiOperation(value = "导入数据字典")
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        dictService.importDictData(file);
        return Result.ok();
    }

    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list=dictService.findChildData(id);
        return Result.ok(list);
    }

    //根据dictcode和value查询name
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,@PathVariable String value){
        return dictService.getName(dictCode,value);
    }

    //根据value查询
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable String value){
        return dictService.getName("",value);
    }

    //根据dictCode查询下级节点
    @ApiOperation("根据dictCode查询下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list=dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }
}
