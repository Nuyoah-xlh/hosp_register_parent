package com.xlh.yygh.oss.controller;

import com.xlh.yygh.common.result.Result;
import com.xlh.yygh.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {
    @Autowired
    private FileService fileService;

    //上传文件到阿里云oss
    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file) {
        System.out.println("22222");
        //获取上传文件
        String url = fileService.upload(file);
        return Result.ok(url);
    }
}
