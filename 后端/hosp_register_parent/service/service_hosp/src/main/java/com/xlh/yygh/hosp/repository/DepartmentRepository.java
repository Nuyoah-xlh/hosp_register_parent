package com.xlh.yygh.hosp.repository;

import com.xlh.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    //上传科室
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

}
