package com.xlh.yygh.hosp.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import javafx.scene.control.Pagination;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.xlh.yygh.hosp.mapper")
public class HospConfig {
    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }


}
