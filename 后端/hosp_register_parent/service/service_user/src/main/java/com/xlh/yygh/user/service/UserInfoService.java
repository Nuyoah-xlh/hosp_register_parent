package com.xlh.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlh.yygh.model.user.UserInfo;
import com.xlh.yygh.vo.user.LoginVo;
import com.xlh.yygh.vo.user.UserAuthVo;
import com.xlh.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService {
    //登录
    Map<String, Object> login(LoginVo loginVo);

    //用户认证
    void userAuth(Long userId, UserAuthVo userAuthVo);

    UserInfo getUserInfoById(Long userId);

    //查询用户分页
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    //锁定/解锁
    void lock(Long userId, Integer status);

    Map<String, Object> show(Long userId);

    //认证审批
    void approval(Long userId, Integer authStatus);
}
