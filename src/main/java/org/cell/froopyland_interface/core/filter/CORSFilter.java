package org.cell.froopyland_interface.core.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author yozora
 * Description cors filter
 **/
@Component
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        res.addHeader("Access-Control-Expose-Headers", "Authorization");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,Accept,X-CAF-Authorization-Token,X-API-KEY,Authorization,sessionToken,X-TOKEN");
        if ("OPTIONS".equals(((HttpServletRequest) request).getMethod())) {
            response.getWriter().println("access");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
