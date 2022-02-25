package com.xlh.yygh.hosp.repository;

import com.xlh.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//整合mongodb
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    //判断是否存在数据,按照规范可自动实现
    Hospital getHospitalByHoscode(String hoscode);

    //根据医院名称查询
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
