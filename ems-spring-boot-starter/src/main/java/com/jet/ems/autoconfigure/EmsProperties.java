package com.jet.ems.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author javahuang
 */
@ConfigurationProperties(prefix = "third-service.ems")
public class EmsProperties {

    /**
     * 错误消息收件人
     */
    private String mailTo;
    private String systemName;
    /**
     * 是否激活错误日志发送功能
     */
    private Boolean active = true;
    private String[] excludeResourceSuffix;
    private String[] includeResourceSuffix;

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String[] getExcludeResourceSuffix() {
        return excludeResourceSuffix;
    }

    public void setExcludeResourceSuffix(String[] excludeResourceSuffix) {
        this.excludeResourceSuffix = excludeResourceSuffix;
    }

    public String[] getIncludeResourceSuffix() {
        return includeResourceSuffix;
    }

    public void setIncludeResourceSuffix(String[] includeResourceSuffix) {
        this.includeResourceSuffix = includeResourceSuffix;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
