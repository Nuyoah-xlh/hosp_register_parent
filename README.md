# hosp_register_parent
仿114网上预约挂号平台的项目，前端采用vue及相关框架、后端为java，作为一个入门的学习项目。

## 部分界面展示

![20210121192316851.png](https://cdn.jsdelivr.net/gh/Nuyoah-xlh/jsDelivr-CDN/img/article_pic/Snipaste_2022-02-26_10-04-09.jpg)

![20210121192316851.png](https://cdn.jsdelivr.net/gh/Nuyoah-xlh/jsDelivr-CDN/img/article_pic/Snipaste_2022-02-26_10-04-30.jpg)

![20210121192316851.png](https://cdn.jsdelivr.net/gh/Nuyoah-xlh/jsDelivr-CDN/img/article_pic/Snipaste_2022-02-26_10-05-41.jpg)

![20210121192316851.png](https://cdn.jsdelivr.net/gh/Nuyoah-xlh/jsDelivr-CDN/img/article_pic/Snipaste_2022-02-26_10-06-58.jpg)

## 功能概述

* 医院设置管理
  * 医院设置列表、添加、锁定、删除
  * 医院列表、详情、排班、下线
* 数据字典
  * 数据字典树形显示、导入、导出
* 用户管理
  * 用户列表、查看、锁定
  * 认证用户审批
* 订单管理
  * 订单列表、详情
* 用户实名认证
* 就诊人管理
* 预约挂号功能

## 技术栈

* SpringBoot
* SpringCloud
  * Nacos注册中心
  * Feign
  * GateWay
* Redis
  * 使用Redis作为数据字典缓存
  * 此外，还可作为验证码等的有效时间（暂未开发）
* MongoDB
  * 使用MongoDB存储 医院相关数据
* EasyExcel
  * 操作excel表格，进行读和写操作
* MyBatisPlus
* RabbitMQ
  * 订单相关操作，发送mq消息
* Linux-Centos7虚拟机，用于redis、mongoDB等数据库的远程调用
* Docker
  * 下载镜像创建容器，如redis、mongoDB、RabbitMQ等都在docker上运行
* 阿里云OSS
  * 上传用户身份证件，并返回访问地址



