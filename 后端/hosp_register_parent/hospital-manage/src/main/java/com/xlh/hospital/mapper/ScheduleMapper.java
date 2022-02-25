package com.xlh.hospital.mapper;

import com.xlh.hospital.model.Schedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ScheduleMapper extends BaseMapper<Schedule> {

}
