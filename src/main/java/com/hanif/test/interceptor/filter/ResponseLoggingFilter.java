package com.hanif.test.interceptor.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.hanif.test.util.EngineUtils;
import com.hanif.test.util.PojoJsonMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author Hanif al kamal 7/30/23
 * @contact hanif.alkamal@gmail.com
 */

@Component
public class ResponseLoggingFilter implements Filter {

    private final Logger logger = LogManager.getLogger(ResponseLoggingFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestId;
        try {
            ResponseWrapper wrappedResponse = new ResponseWrapper(httpResponse);

            filterChain.doFilter(httpRequest, wrappedResponse);

            byte[] responseBody = wrappedResponse.getContentAsByteArray();
            String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);
            requestId = (String) servletRequest.getAttribute("requestId");

            logger.info("Response[" + requestId + "]: " + httpResponse.getStatus() + " " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());
            logger.info("Response Body[" + requestId + "]: \n" + responseBodyString);

            wrappedResponse.copyBodyToResponse();
        } catch (Exception e) {
            requestId = (String) servletRequest.getAttribute("requestId");
            httpResponse.setStatus(500);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            String json = PojoJsonMapper.toJson(EngineUtils.createFailedReponse(500, e.getMessage(), httpRequest.getPathInfo()));

            logger.info("Response[" + requestId + "]: " + httpResponse.getStatus() + " " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());
            logger.info("Response Body[" + requestId + "]: \n" + json);

            httpResponse.getWriter().write(json);
        }
    }

    @Override
    public void destroy() {

    }

    private static class ResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    buffer.write(b);
                }

                @Override
                public boolean isReady() {
                    // TODO: Implement this method if needed
                    throw new UnsupportedOperationException("Not yet implemented");
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                    // TODO: Implement this method if needed
                    throw new UnsupportedOperationException("Not yet implemented");
                }
            };
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(getOutputStream());
        }

        public byte[] getContentAsByteArray() {
            return buffer.toByteArray();
        }

        public void copyBodyToResponse() throws IOException {
            getResponse().getOutputStream().write(buffer.toByteArray());
            getResponse().getOutputStream().flush();
        }
    }
}
