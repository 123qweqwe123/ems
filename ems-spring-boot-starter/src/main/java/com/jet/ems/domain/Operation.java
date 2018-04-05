package com.jet.ems.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * <pre>
 * </pre>
 *
 * @author javahuang
 * @since 2018/2/7 上午10:24
 */
public class Operation {

    /*** 菜单名称*/
    private String menu;
    /*** 菜单链接*/
    private String url;
    /*** 请求类型 GET/POST/PUT/DELETE/PATCH */
    private String method;
    /*** 请求参数*/
    private String request;
    /*** 响应参数*/
    private String response;
    private Boolean hasError = false;
    private String errMsg;
    private String stackTrace;
    private String createBy;
    private String createByName;
    private Date createTime;
    /*** 客户端 IP*/
    private String ip;
    /*** 客户端浏览器类型*/
    private String userAgent;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "<div>您好：</br>&nbsp;&nbsp;&nbsp;您的系统发生了异常。具体错误信息如下</div>" +
                "<div style='margin-left: 20px'><span>菜单名称：" + menu + "</span></div>" +
                "<div style='margin-left: 20px'><span>请求链接：" + url + "</span></div>" +
                "<div style='margin-left: 20px'><span>请求方式：" + method + "</span></div>" +
                "<div style='margin-left: 20px'><span>请求参数：" + request + "</span></div>" +
                "<div style='margin-left: 20px'><span>操作人：" + createBy + "</span></div>" +
                "<div style='margin-left: 20px'><span>操作人名称：" + createByName + "</span></div>" +
                "<div style='margin-left: 20px'><span>操作时间：" + (createTime != null ? df.format(createTime) : null) + "</span></div>" +
                "<div style='margin-left: 20px'><span>操作人IP：" + ip + "</span></div>" +
                "<div style='margin-left: 20px'><span>浏览器：" + userAgent + "</span></div>" +
                "<div style='margin-left: 20px'><span>错误消息：" + errMsg + "</span></div>" +
                "<div style='margin-left: 20px'><code>错误堆栈：" + stackTrace + "</code></div>";
    }
}
