package com.xlh.yygh.hosp.service;

import com.xlh.yygh.model.hosp.Department;
import com.xlh.yygh.vo.hosp.DepartmentQueryVo;
import com.xlh.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    //上传科室
    void save(Map<String, Object> paramMap);

    //查询科室，参数有当前页、每页记录数，及查询条件
    Page<Department> findDepartmentsByPage(int page, int limit, DepartmentQueryVo departmentQueryVo);

    //删除科室
    void removeDept(String hoscode, String depCode);

    //查询医院的所有科室
    List<DepartmentVo> findDeptTree(String hoscode);

    //获取部门名称
    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
