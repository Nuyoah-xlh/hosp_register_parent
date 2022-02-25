package com.xlh.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.xlh.yygh.model.user.Patient;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientMapper extends BaseMapper<Patient> {
}
