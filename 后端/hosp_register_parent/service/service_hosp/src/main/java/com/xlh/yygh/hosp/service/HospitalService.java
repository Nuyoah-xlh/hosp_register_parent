package com.xlh.yygh.hosp.service;

import com.xlh.yygh.model.hosp.Hospital;
import com.xlh.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    //上传医院接口
    void save(Map<String, Object> paramMap);

    //根据医院编号查询
    Hospital getHospitalByHoscode(String hoscode);

    //根据条件分页查询
    Page<Hospital> selectHospPage(int page, int limit, HospitalQueryVo hospitalQueryVo);

    //更新医院状态
    void updateStatus(String id, Integer status);

    //获取医院详细信息
    Map<String, Object> getHospById(String id);

    //根据医院名称查询
    List<Hospital> findByHosname(String hosname);

    //根据医院编号获取预约挂号详情
    Map<String, Object> item(String hoscode);
}
