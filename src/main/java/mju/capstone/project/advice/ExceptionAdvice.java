package mju.capstone.project.advice;

import mju.capstone.project.exception.DummyNotFoundException;
import mju.capstone.project.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(DummyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response dummyNotFoundException() {
        return Response.failure(404, "해당 더미를 찾을 수 없습니다.");
    }
}
