package cz.muni.pa165.surrealtravel.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Petr Dvořák [359819]
 */
public class SasAllowOriginInterceptor extends HandlerInterceptorAdapter {

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return true
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        return true;
    }
    
}
