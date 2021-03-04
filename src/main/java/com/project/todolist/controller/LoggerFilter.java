package com.project.todolist.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.http.HttpRequest;

@Component
public class LoggerFilter implements Filter {
    private static final Logger  logger = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    if(servletRequest instanceof HttpServletRequest)
        {
            var httpServlet = (HttpServletRequest)servletRequest;
            logger.info("doFiler " + httpServlet.getMethod() + " " +httpServlet.getRequestURI());
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

}
