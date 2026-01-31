package com.fastprep.site.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        
        // Public routes
        if (uri.equals("/") || uri.startsWith("/auth/") || uri.startsWith("/static/") || 
            uri.startsWith("/css/") || uri.startsWith("/images/") || uri.startsWith("/js/")) {
            return true;
        }
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("/auth/login");
            return false;
        }
        
        return true;
    }
}
