package com.xlh.EasyExcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class testWrite {
    public static void main(String[] args) {
        //构造数据集合
        List<UserData> userData=new ArrayList<>();
        for(int i=0;i<100;i++){
            UserData userData1=new UserData();
            userData1.setUid(i);
            userData1.setUsername("xlh"+i);
            userData.add(userData1);
        }

        //设置excel文件路径及名称
        String fileName="D:\\01.xlsx";

        //进行写操作
        EasyExcel.write(fileName,UserData.class).sheet("用户列表").doWrite(userData);

    }
}
