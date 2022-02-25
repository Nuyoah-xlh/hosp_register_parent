package com.xlh.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlh.yygh.cmn.client.DictFeignClient;
import com.xlh.yygh.hosp.repository.HospitalRepository;
import com.xlh.yygh.hosp.service.HospitalService;
import com.xlh.yygh.model.hosp.Hospital;
import com.xlh.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.net.ssl.HostnameVerifier;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    //上传/修改医院信息
    @Override
    public void save(Map<String, Object> paramMap) {
        //类型转换
        String mapString= JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString,Hospital.class);
        //判断是否存在该医院
        Hospital hospital1=hospitalRepository.getHospitalByHoscode(hospital.getHoscode());

        //存在则更新
        if(hospital1!=null){
            hospital.setStatus(hospital1.getStatus());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
        //不存在则添加
        else{
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    //根据hoscode查询医院
    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    //根据条件分页查询
    @Override
    public Page<Hospital> selectHospPage(int page, int limit, HospitalQueryVo hospitalQueryVo) {
        //创建pageable对象
        Pageable pageable = PageRequest.of(page-1,limit);
        //创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //hospitalSetQueryVo转换Hospital对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        //创建对象
        Example<Hospital> example = Example.of(hospital,matcher);
        //调用方法实现查询
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        //获取查询list集合，遍历进行医院等级封装
        pages.getContent().stream().forEach(item -> {
            this.setHospitalHostype(item);
        });

        return pages;
    }

    //设置医院类型
    private Hospital setHospitalHostype(Hospital item) {
        //医院的dictcode为hostype,根据两个参数查询
        String hosTypeString=dictFeignClient.getName("Hostype",item.getHostype());
        //查询省市区
        String provinceString=dictFeignClient.getName(item.getProvinceCode());
        String cityString=dictFeignClient.getName(item.getCityCode());
        String districtString=dictFeignClient.getName(item.getDistrictCode());

        //添加参数
        //医院级别：三甲、二甲
        item.getParam().put("hostypeString",hosTypeString);
        //地址全称
        item.getParam().put("fullAddress",provinceString+cityString+districtString);

        return item;
    }

    //更新医院状态
    @Override
    public void updateStatus(String id, Integer status) {
        //根据id查询医院信息
        Hospital hospital=hospitalRepository.findById(id).get();
        //设置相关值
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Map<String, Object> map=new HashMap<>();
        //获取完整医院信息（包括等级）
        Hospital hospital=this.setHospitalHostype(hospitalRepository.findById(id).get());
        //放到map中
        map.put("hospital",hospital);
        //预约规则
        map.put("bookingRule",hospital.getBookingRule());
        return map;
    }

    //根据医院名称查询
    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    //根据医院编号获取预约挂号详情
    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> map=new HashMap<>();
        //获取完整医院信息（包括等级）
        Hospital hospital=this.setHospitalHostype(this.getHospitalByHoscode(hoscode));
        //放到map中
        map.put("hospital",hospital);
        //预约规则
        map.put("bookingRule",hospital.getBookingRule());
        return map;
    }
}
