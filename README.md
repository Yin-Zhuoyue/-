<h1 align="center">Auto-QueryDataTask-System</h1>

## 项目说明
企业中的数据分析人员会有很多常规的取数需求，需要通过sql在企业的数据库中查清单或者统计数据来为自己的报告、材料做支撑。对于定期的sql取数，该系统提供了一个自动化的流程，用户可以方便地配置一个自动取数的任务，然后通过邮箱、ftp等方式获取。
该项目的前端使用 **Vue** 框架来实现，服务端使用 **Spring Boot + MyBatis** 来实现，数据库使用了 **MySQL Oracle**。

<br/>

## 项目预览

> 前端截图预览

|![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview1.png)   | ![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview2.png)  |
|---|---|
|![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview3.png)   |  ![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview4.png) |
|![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview5.png)   | ![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview6.png)  |
|![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview7.png)   |  ![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview8.png) |
<br/>


> 后端代码截图预览

|![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview9.png)   | ![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview10.png)  |
|---|---|
|![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview11.png)   |  ![](https://github.com/Yin-Zhuoyue/Auto-QueryDataTask-System/blob/main/img/preview12.png) |


## 项目功能

- 用户登录，分权分域
- 配置自动化取数任务
- 测试sql脚本

<br/>

## 技术栈

### 后端

**SpringBoot + MyBatis + druid + quartz + dynamic-datasource**

### 前端

**Vue3.0 + Vue-Router + Vuex + Axios + ElementUI**

<br/>

## 开发环境

JDK：jdk1.8.0_102

mysql：mysql-8.0.19

node：16.16.0

IDE：IntelliJ IDEA 2021、VSCode


<br/>

### 3、修改配置文件

1）创建数据库
<br>
将 `Auto-QueryDataTask-System/backend/sql` 文件夹中的 `auto_task_system.sql` 文件导入数据库。

2）修改数据库相关配置
<br>
1.修改 `Auto-QueryDataTask-System/back-end/src/main/resources/application.yml` 文件里的mysql数据库的用户名和密码（此数据库为系统的主数据库）。
<br>
2.修改 `Auto-QueryDataTask-System/back-end/src/main/resources/application.yml` 文件里的其他几个数据库的配置，驱动、用户名密码等等（这些是用户需要从中取业务数据的数据库）

### 4、启动项目

- **启动后端**：进入 back-end 文件夹，运行下面命令启动服务器
```js
mvn spring-boot:run
```
- **启动前端**：进入 front-end 目录，运行下面命令

```js
npm install // 安装依赖

npm run serve // 启动前台项目
```
