package org.example.productservice.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.productservice.exception.AppException;
import org.example.productservice.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthUtils {

    public Long getCurrentUserId() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        HttpServletRequest request = attrs.getRequest();
        String userIdHeader = request.getHeader("X-User-Id");

        if (userIdHeader == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return Long.valueOf(userIdHeader);
    }
}
