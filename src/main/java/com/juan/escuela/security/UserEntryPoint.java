package com.juan.escuela.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juan.escuela.dto.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class UserEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper OBJECT_MAPPER;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        ErrorDto errorDto = new ErrorDto();
        String endpoint = request.getRequestURI();
        boolean isLogin = endpoint.substring(11).equals("/auth/login");

        if (isLogin) {
            errorDto.add("mensaje", "la contraseña ingresada es incorrecta");
        } else {
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()){
                errorDto.add("mensaje", "no ha ingresado ningún token");
            } else {
                errorDto.add("mensaje", "el token ingresado es incorrecto");
            }
        }

        OBJECT_MAPPER.writeValue(response.getOutputStream(),errorDto);
    }
}
