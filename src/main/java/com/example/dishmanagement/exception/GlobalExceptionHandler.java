package com.example.dishmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Không để lỗi hệ thống hiện thẳng ra màn hình: luôn trả về trang thông báo thân thiện (có đa ngôn ngữ). */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "error.notfound.title", ex.getMessageKey(), ex.getArg());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleBadId(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.NOT_FOUND, "error.notfound.title", "error.id.invalid", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResource(NoResourceFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "error.notfound.title", "error.page.notfound", null);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleOther(Exception ex) {
        ex.printStackTrace();
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "error.title", "error.generic", null);
    }

    private ModelAndView build(HttpStatus status, String titleKey, String messageKey, String arg) {
        ModelAndView mv = new ModelAndView("error");
        mv.setStatus(status);
        mv.addObject("titleKey", titleKey);
        mv.addObject("messageKey", messageKey);
        mv.addObject("messageArg", arg);
        return mv;
    }
}
