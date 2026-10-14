package com.example.dishmanagement.exception;

import lombok.Getter;

/** Id không tồn tại. messageKey là khóa trong messages*.properties để hiển thị đúng ngôn ngữ. */
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String messageKey;
    private final String arg;

    public ResourceNotFoundException(String messageKey, Object arg) {
        super(messageKey);
        this.messageKey = messageKey;
        this.arg = String.valueOf(arg);
    }
}
