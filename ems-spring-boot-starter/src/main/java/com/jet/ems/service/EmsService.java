package com.jet.ems.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * <pre>
 * </pre>
 *
 * @author javahuang
 * @since 2018/2/7 上午10:37
 */
public interface EmsService {

    /**
     * 通过 url 来获取菜单名称
     *
     * @param url
     * @return
     */
    String getMenuByUrl(String url);

    /**
     * 获取操作人 ID
     *
     * @param request
     * @return
     */
    String getCreateBy(HttpServletRequest request);

    /**
     * 当前操作人名称
     *
     * @param request
     * @return
     */
    String getCreateByName(HttpServletRequest request);
}
