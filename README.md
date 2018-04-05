## 错误日志发送服务


###

### ems-spring-boot-starter 使用
与系统集成，将错误信息发送到指定的人

* 添加依赖

    ``` xml
    <dependency>
        <groupId>com.jet</groupId>
        <artifactId>ems-spring-boot-starter</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
* 配置
    - `third-service.mail.server-request-url` 邮件服务地址
    - `third-service.ems.mail-to` 错误消息接收人
    - `third-service.ems.system-name` 当前系统名称

## 注意事项

1、 记录当前用户相关信息 必须创建
`EmsService`的实现bean，日志审计系统会自动调用该 bean 的相应方法设置值

- 当前登录用户名称
- 当前登录用户 ID
- 通过当前请求来获取请求对应的菜单名称

2、对于有自定义异常处理的系统，如样本 web 端 `ServiceExceptionHandler`，需要在
`@ExceptionHadler` 方法里面调用 `ErrorMsgFilter.sendError(errMsg);` 将异常发送出去




