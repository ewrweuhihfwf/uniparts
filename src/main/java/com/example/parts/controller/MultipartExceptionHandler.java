package com.example.parts.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class MultipartExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(MultipartExceptionHandler.class);

    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class})
    public String handleMultipartException(HttpServletRequest request, Exception exception) {
        String requestUri = request.getRequestURI();
        log.warn("Multipart upload failed for request {}", requestUri, exception);

        if (requestUri != null && requestUri.startsWith("/ads/") && requestUri.endsWith("/edit")) {
            return "redirect:" + requestUri + "?uploadError";
        }
        if ("/sell".equals(requestUri)) {
            return "redirect:/sell?uploadError";
        }
        return "redirect:/?uploadError";
    }
}
