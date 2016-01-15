package ru.goodfil.catalog.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: IE9CompabilityFilter.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class IE9CompabilityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse) response).setHeader("X-UA-Compatible", "IE=EmulateIE8");
        ((HttpServletResponse) response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        ((HttpServletResponse) response).setHeader("Pragma", "no-cache");
        ((HttpServletResponse) response).setHeader("Expires", "0");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
