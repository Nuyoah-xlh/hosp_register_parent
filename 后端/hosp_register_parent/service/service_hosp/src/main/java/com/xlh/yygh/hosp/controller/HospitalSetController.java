package com.xlh.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.mysql.jdbc.util.ResultSetUtil;
import com.xlh.yygh.common.exception.YyghException;
import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.common.utils.MD5;
import com.xlh.yygh.hosp.service.HospitalSetService;
import com.xlh.yygh.model.hosp.HospitalSet;
import com.xlh.yygh.vo.hosp.HospitalQueryVo;
import com.xlh.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;
import java.util.Random;


@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //查找表内所有数据
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/findAll")
    public Result findAll(){
        List<HospitalSet> hospitalSetList=hospitalSetService.list();
        return Result.ok(hospitalSetList);
    }

    //根据删除id删除数据
    @ApiOperation(value = "根据id逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Result removeHospSet(@PathVariable long id){
        boolean flag=hospitalSetService.removeById(id);
        if(flag){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }

    @ApiOperation(value = "条件查询带分页")
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long  current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalQueryVo){
        //创建page对象，传递当前页，每页的记录数
        Page<HospitalSet> page=new Page<>(current,limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper=new QueryWrapper<>();
        String hosname = hospitalQueryVo.getHosname();
        String hoscode=hospitalQueryVo.getHoscode();
        if(!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hospitalQueryVo.getHosname());
        }
        if(!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode",hospitalQueryVo.getHoscode());
        }
        //查询
        Page<HospitalSet> hospitalSetPage=hospitalSetService.page(page,wrapper);
        //返回
        return Result.ok(hospitalSetPage);
    }

    //添加医院设置
    @ApiOperation(value = "添加医院设置")
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
        //设置状态1为可用，0为不可用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random=new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));
        //调用service
        boolean save=hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }

    //根据id获取医院设置
    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("/getHospSet/{id}")
    public Result getHospSet(@PathVariable long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        if(hospitalSet!=null){
            return Result.ok(hospitalSet);
        }
        else{
            return Result.fail();
        }
    }

    //修改医院设置
    @ApiOperation(value = "修改医院设置")
    @PostMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag=hospitalSetService.updateById(hospitalSet);
        if(flag){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }

    //批量删除医院设置
    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("/batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList){
        boolean flag=hospitalSetService.removeByIds(idList);
        if(flag){
            return Result.ok();
        }
        else{
            return Result.fail();
        }
    }

    //医院设置锁定和解锁
    @ApiOperation(value = "医院设置锁定和解锁")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable long id,
                                  @PathVariable int status){
        //根据id查询
        HospitalSet hospitalSet=hospitalSetService.getById(id);
        //设定状态
        hospitalSet.setStatus(status);
        //更新
        boolean flag=hospitalSetService.updateById(hospitalSet);
        if(flag){
            return Result.ok();
        }
        else {
            return Result.fail();
        }
    }

    //发送签名密钥
    @ApiOperation(value = "发送签名密钥")
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable long id){
        HospitalSet hospitalSet=hospitalSetService.getById(id);
        if(hospitalSet!=null){
            String signKey=hospitalSet.getSignKey();
            String hoscode=hospitalSet.getHoscode();
            // TODO 发送短信
            return Result.ok();
        }
        else {
            return Result.fail();
        }
    }


}

