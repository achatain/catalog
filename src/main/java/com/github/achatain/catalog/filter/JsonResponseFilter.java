package com.github.achatain.catalog.filter;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by antoine on 12/02/2017.
 **/
@Singleton
public class JsonResponseFilter implements Filter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse httpResp = (HttpServletResponse) response;
        httpResp.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString());

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no-op
    }
}
