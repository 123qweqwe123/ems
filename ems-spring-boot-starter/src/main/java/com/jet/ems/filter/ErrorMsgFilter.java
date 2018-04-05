package com.jet.ems.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jet.ems.autoconfigure.EmsProperties;
import com.jet.ems.domain.Operation;
import com.jet.ems.service.EmsService;
import com.jet.mail.domain.MailEntity;
import com.jet.mail.service.MailService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Description:
 * <pre>
 * </pre>
 *
 * @author javahuang
 * @since 2018/2/7 上午10:19
 */
public class ErrorMsgFilter implements Filter, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ErrorMsgFilter.class);

    private final EmsProperties properties;

    private static ThreadLocal<Operation> operationThreadLocal = new ThreadLocal<>();
    private ObjectMapper jsonMapper = new ObjectMapper();
    private ApplicationContext applicationContext;
    private EmsService emsService;

    public ErrorMsgFilter(EmsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (!properties.getActive()) {
            return;
        }
        if(StringUtils.isEmpty(properties.getMailTo())) {
            logger.error("未配置 third-service.ems.mail-to,错误日志将不能够被发送");
            properties.setActive(false);
        }
        logger.info("错误日志记录服务启动...");
        if (applicationContext != null) {
            try {
                emsService = applicationContext.getBean(EmsService.class);
            } catch (NoSuchBeanDefinitionException e) {
                logger.warn("未发现 EmsService 实现类，操作人，操作时间，菜单名称将不可用！！！");
            }
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!properties.getActive()) {
            chain.doFilter(request, response);
        } else {
            Operation operation = new Operation();
            operation.setCreateTime(new Date());
            operationThreadLocal.set(operation);
            try {
                buildOperationWithRequest(request);
                buildOperationSystemData(request, operation);
                chain.doFilter(request, response);
            } catch (Exception e) {
                // 只是捕获异常，并不对异常进一步处理，所以要接着抛出
                sendError(e);
                throw e;
            } finally {
                if (operation.getHasError()) {
                    MailEntity entity = new MailEntity();
                    List<String> to = Lists.newArrayList();
                    to.add(properties.getMailTo());
                    entity.setTo(to);
                    entity.setContent(operation.toString());
                    entity.setSubject(properties.getSystemName() + "运行错误");
                    MailService mailService = applicationContext.getBean(MailService.class);
                    mailService.send(entity);
                }
                operationThreadLocal.remove();
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * 解析请求来获取相应的元数据
     *
     * @param request
     */
    private void buildOperationWithRequest(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Operation operation = operationThreadLocal.get();
        if (operation != null) {
            operation.setUrl(httpServletRequest.getRequestURI());
            operation.setUserAgent(httpServletRequest.getHeader("User-Agent"));
            operation.setIp(request.getRemoteAddr());
            try {
                operation.setMethod(httpServletRequest.getMethod());
                String requestStr = getRequestParameter(httpServletRequest);
                operation.setRequest(requestStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前请求的 parameter
     *
     * @param httpServletRequest
     * @return
     * @throws JsonProcessingException
     */
    private String getRequestParameter(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        Enumeration<String> names = httpServletRequest.getParameterNames();
        Map<String, String> paramsMap = new HashMap<>(16);
        while (names.hasMoreElements()) {
            String paramName = names.nextElement();
            String paramValue = httpServletRequest.getParameter(paramName);
            paramsMap.put(paramName, paramValue);
        }
        return jsonMapper.writeValueAsString(paramsMap);
    }

    /**
     * 获取系统相关用户信息
     *
     * @param request
     * @param operation
     */
    private void buildOperationSystemData(ServletRequest request, Operation operation) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (emsService != null) {
            operation.setMenu(emsService.getMenuByUrl(operation.getUrl()));
            operation.setCreateBy(emsService.getCreateBy(httpServletRequest));
            operation.setCreateByName(emsService.getCreateByName(httpServletRequest));
        }
    }

    /**
     * 如果使用了 @ExceptionHandler 处理异常，filter 是不能获取到异常信息的
     * 可以在 @ExceptionHandler 将异常发送到当前的 filter
     *
     * @param msg 异常信息
     */
    public static void sendError(String msg) {
        Operation operation = operationThreadLocal.get();
        if (operation != null) {
            operation.setHasError(true);
            operation.setErrMsg(msg);
        }
    }

    public static void sendError(String msg, String stackTrace) {
        Operation operation = operationThreadLocal.get();
        if (operation != null) {
            operation.setHasError(true);
            operation.setErrMsg(msg);
            operation.setStackTrace(stackTrace);
        }
    }

    public static void sendError(Throwable error) {
        Operation operation = operationThreadLocal.get();
        if (error != null) {
            operation.setHasError(true);
            operation.setErrMsg(error.getMessage());
            operation.setStackTrace(ExceptionUtils.getStackTrace(error));
        }
    }

    /**
     * 不匹配指定类型的 url
     *
     * @param url
     * @return
     */
    private boolean excludeResources(String url) {
        String[] excludeResources = properties.getExcludeResourceSuffix();
        if (excludeResources == null || excludeResources.length == 0) {
            return false;
        }
        for (String excludeResource : excludeResources) {
            if (StringUtils.endsWithIgnoreCase(url, excludeResource)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 只匹配指定类型的 url
     *
     * @param url
     * @return
     */
    private boolean includeResources(String url) {
        String[] excludeResources = properties.getIncludeResourceSuffix();
        if (excludeResources == null || excludeResources.length == 0) {
            return true;
        }
        for (String excludeResource : excludeResources) {
            if (StringUtils.endsWithIgnoreCase(url, excludeResource)) {
                return true;
            }
        }
        return false;
    }

}
