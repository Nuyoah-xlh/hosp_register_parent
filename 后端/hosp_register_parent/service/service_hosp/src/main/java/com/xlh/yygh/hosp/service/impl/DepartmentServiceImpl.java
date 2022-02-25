package com.xlh.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlh.yygh.hosp.repository.DepartmentRepository;
import com.xlh.yygh.hosp.service.DepartmentService;
import com.xlh.yygh.model.hosp.Department;
import com.xlh.yygh.vo.hosp.DepartmentQueryVo;
import com.xlh.yygh.vo.hosp.DepartmentVo;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    //上传科室
    @Override
    public void save(Map<String, Object> paramMap) {
        //先转为department对象
        String dept= JSONObject.toJSONString(paramMap);
        Department department=JSONObject.parseObject(dept,Department.class);
        //根据医院id和科室id添加
        Department departmentExist= departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());
        if(departmentExist!=null){
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        }
        else{
            Date date=new Date();
            department.setCreateTime(date);
            department.setUpdateTime(date);
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    //查询科室
    @Override
    public Page<Department> findDepartmentsByPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //创建分页对象，设置页数和记录数，0是第一页
        Pageable pageable=PageRequest.of(page-1, limit);
        //创建Example对象
        ExampleMatcher exampleMatcher=ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        //对象类型转换
        Department department=new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        Example<Department> example=Example.of(department,exampleMatcher);
        //查询
        return departmentRepository.findAll(example,pageable);
    }

    //删除科室
    @Override
    public void removeDept(String hoscode, String depCode) {
        //先查询是否存在该科室
        Department department=departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depCode);
        if(department!=null){
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建list集合用于封装
        List<DepartmentVo> departmentVoList=new ArrayList<>();
        //根据医院编号，查询所有科室
        Department department=new Department();
        department.setHoscode(hoscode);
        Example example=Example.of(department);
        List<Department> all=departmentRepository.findAll(example);
        Map<String, List<Department>> collect = all.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历，进行封装
        for(Map.Entry<String, List<Department>> entry:collect.entrySet()){
            //大科室编号
            String bigCode=entry.getKey();
            //对应的所有小科室
            List<Department> departmentList=entry.getValue();
            //封装大科室
            DepartmentVo departmentVo=new DepartmentVo();
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(departmentList.get(0).getBigname());

            //封装小科室
            List<DepartmentVo> voList=new ArrayList<>();
            for(Department department1:departmentList){
                DepartmentVo departmentVo1=new DepartmentVo();
                departmentVo1.setDepname(department1.getDepname());
                departmentVo1.setDepcode(department1.getDepcode());
                voList.add(departmentVo1);
            }
            //整合
            departmentVo.setChildren(voList);
            departmentVoList.add(departmentVo);

        }
        return departmentVoList;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode).getDepname();
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
    }
}
