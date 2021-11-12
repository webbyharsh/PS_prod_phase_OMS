package com.ps.oms.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.oms.auth.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler{

    @Autowired
    private FilterExceptionMessage filterExceptionMessage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), filterExceptionMessage.getMessage());
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(errorResponse);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(json);
            log.info("Error Message is successfully written to response");

    }
}
