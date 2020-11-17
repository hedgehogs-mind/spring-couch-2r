package com.hedgehogsmind.springcouch2r.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class Couch2rResponseUtil {

    public static void writeResponseEntity(
            final ResponseEntity responseEntity,
            final HttpServletResponse response,
            final ObjectMapper objectMapper
            ) {

        response.setStatus(responseEntity.getStatusCodeValue());
        responseEntity.getHeaders().forEach((name, value) -> {
            response.setHeader(name, value.stream().collect(Collectors.joining(", ")));
        });

        final Object body = responseEntity.getBody();

        if ( body != null ) {
            String bodyToWrite = null;

            if ( body instanceof String ||
                    body instanceof Boolean ||
                    body instanceof Number ) {

                bodyToWrite = String.valueOf(body);
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            } else {
                try {
                    bodyToWrite = objectMapper.writeValueAsString(body);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                } catch ( JsonProcessingException e ) {
                    throw new RuntimeException("Could not write body as JSON", e);
                }
            }

            if ( bodyToWrite == null ) throw new IllegalStateException("Body stringify failed.");

            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            try {
                response.getWriter().print(bodyToWrite);
            } catch ( IOException e ) {
                throw new RuntimeException("Could not write response body", e);
            }
        }
    }

}