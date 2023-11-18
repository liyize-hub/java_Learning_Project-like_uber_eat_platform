package com.kidd.uber_eat_demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson.JSON;
import com.kidd.uber_eat_demo.common.R;

import lombok.extern.slf4j.Slf4j;

/**
 * check the user login or not
 */
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的 URI
        String requsetURL = request.getRequestURI();

        log.info("拦截到请求: {}", requsetURL);

        // 定义不需要处理的请求
        String[] urls = new String[] {
                "/employee/login",
                "/emloyee/logout",
                "/backend/**",
                "/front/**"
        };

        // 2. 判断本次请求, 是否需要登录, 才可以访问
        boolean check = check(urls, requsetURL);

        // 3. 如果不需要，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requsetURL);
            chain.doFilter(request, response);
            return;
        }

        // 4. 判断登录状态，如果已登录，则直接放行
        Object attribute = request.getSession().getAttribute("employee");
        if (attribute != null) {
            log.info("用户已登录，用户id为：{}", attribute);
            chain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        // 5. 如果未登录, 则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * 
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
