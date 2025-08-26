package org.example.identityservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(404, "User Not Found", HttpStatus.NOT_FOUND),
    USER_EXISTS(409, "User Already Exists", HttpStatus.BAD_REQUEST),
    ROLE_EXISTS(409, "ROLE Already Exists", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(400, "Invalid Password", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(403, "You don't have permission", HttpStatus.FORBIDDEN),
    ;;


    private int code;
    private String message;
    private HttpStatus httpStatus;
    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
