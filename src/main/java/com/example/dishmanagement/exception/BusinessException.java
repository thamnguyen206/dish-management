package com.example.dishmanagement.exception;

import lombok.Getter;

/** Vi phạm quy tắc nghiệp vụ (URL ảnh sai, xóa loại đang có món ...). */
@Getter
public class BusinessException extends RuntimeException {
    private final String messageKey;
    private final String arg;

    public BusinessException(String messageKey) {
        this(messageKey, null);
    }

    public BusinessException(String messageKey, String arg) {
        super(messageKey);
        this.messageKey = messageKey;
        this.arg = arg;
    }
}
