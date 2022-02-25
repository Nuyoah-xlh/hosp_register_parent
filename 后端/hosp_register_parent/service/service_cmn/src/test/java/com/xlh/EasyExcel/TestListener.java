package com.xlh.EasyExcel;

import com.alibaba.excel.EasyExcel;

public class TestListener {
    public static void main(String[] args) {
        //excel的路径
        String filename="D:\\01.xlsx";
        //读取
        EasyExcel.read(filename,UserData.class,new ExcelListener()).sheet().doRead();
    }
}
