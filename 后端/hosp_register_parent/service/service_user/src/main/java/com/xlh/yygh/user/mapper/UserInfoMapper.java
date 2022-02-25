package com.xlh.yygh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlh.yygh.model.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
