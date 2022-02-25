package com.xlh.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlh.yygh.cmn.client.DictFeignClient;
import com.xlh.yygh.enums.DictEnum;
import com.xlh.yygh.model.user.Patient;
import com.xlh.yygh.user.mapper.PatientMapper;
import com.xlh.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {
    @Autowired
    private DictFeignClient dictFeignClient;

    //获取就诊人列表
    @Override
    public List<Patient> findAllUserId(Long userId) {
        //根据userid查询所有就诊人信息列表
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);
        //通过远程调用，得到编码对应具体内容，查询数据字典表内容
        patientList.stream().forEach(item -> {
            //其他参数封装
            this.packPatient(item);
        });
        return patientList;

    }

    //参数封装
    private Patient packPatient(Patient item) {
        //根据证件类型编码，获取证件类型具体指
        String certificatesTypeString = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), item.getCertificatesType());//联系人证件
        //联系人证件类型
        String contactsCertificatesTypeString = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), item.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(item.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(item.getCityCode());
        //区
        String districtString = dictFeignClient.getName(item.getDistrictCode());
        item.getParam().put("certificatesTypeString", certificatesTypeString);
        item.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        item.getParam().put("provinceString", provinceString);
        item.getParam().put("cityString", cityString);
        item.getParam().put("districtString", districtString);
        item.getParam().put("fullAddress", provinceString + cityString + districtString + item.getAddress());
        return item;
    }

    //根据id获取就诊人信息
    @Override
    public Patient getPatientId(Long id) {
        return this.packPatient(baseMapper.selectById(id));
    }
}
