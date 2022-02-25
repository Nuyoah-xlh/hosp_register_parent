package com.xlh.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlh.yygh.common.exception.YyghException;
import com.xlh.yygh.common.result.ResultCodeEnum;
import com.xlh.yygh.hosp.mapper.HospitalSetMapper;
import com.xlh.yygh.hosp.service.HospitalSetService;
import com.xlh.yygh.model.hosp.Hospital;
import com.xlh.yygh.model.hosp.HospitalSet;
import com.xlh.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("hoscode",hoscode);
        HospitalSet hospital= baseMapper.selectOne(queryWrapper);
        return hospital.getSignKey();
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }
}
