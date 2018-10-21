package com.ciusers.controller.error;

import com.ciusers.error.ErrorCode;
import com.ciusers.error.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleError(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), ErrorCode.GENERAL_ERROR));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleError404(HttpServletRequest request, Exception e) {
        return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), ErrorCode.NOT_FOUND));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        return ResponseEntity.badRequest().body(new ErrorMessage("Field " + fieldErrors.get(0).getField() + "is invalid", ErrorCode.INVALID_ARGUMENT));
    }
}
