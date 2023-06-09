package mju.capstone.project.advice;

import mju.capstone.project.exception.board.BoardNotFoundException;
import mju.capstone.project.exception.board.SerialNumberExistException;
import mju.capstone.project.exception.board.WriterNotMatchException;
import mju.capstone.project.exception.category.CategoryListEmptyException;
import mju.capstone.project.exception.category.CategoryNotFoundException;
import mju.capstone.project.exception.image.ImageUploadFailureException;
import mju.capstone.project.exception.image.NotSupportedExtensionException;
import mju.capstone.project.exception.refreshToken.RefreshTokenNotFoundException;
import mju.capstone.project.exception.user.LoginFailureException;
import mju.capstone.project.exception.user.UserDuplicateException;
import mju.capstone.project.exception.user.UserNotFoundException;
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

    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response boardNotFoundException() {
        return Response.failure(404, "입력하신 정보와 일치하는 게시글을 찾을 수 없습니다.");
    }

    @ExceptionHandler(WriterNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response writerNotMatchException() {
        return Response.failure(400, "현재 로그인한 사용자와 작성자가 일치하지 않습니다.");
    }

    @ExceptionHandler(SerialNumberExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response serialNumberExistException() {
        return Response.failure(400, "입력하신 번호는 제품의 고유한 일련번호이므로, QR 코드를 제공하지 않습니다.");
    }

    @ExceptionHandler(NotSupportedExtensionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response notSupportedExtensionException(NotSupportedExtensionException e) {
        return Response.failure(400, e.getMessage());
    }

    @ExceptionHandler(ImageUploadFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response imageUploadFailureException(ImageUploadFailureException e) {
        return Response.failure(400, e.getMessage());
    }

    @ExceptionHandler(CategoryListEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryListEmptyException() {
        return Response.failure(404, "카테고리가 비어있습니다.");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException() {
        return Response.failure(404, "입력하신 id에 해당하는 카테고리를 찾지 못하였습니다.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response userNotFoundException() {
        return Response.failure(404, "입력하신 정보와 일치하는 사용자가 존재하지 않습니다.");
    }

    @ExceptionHandler(UserDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response userDuplicateException(UserDuplicateException e) {
        return Response.failure(409, e.getMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response loginFailureException() {
        return Response.failure(400, "로그인에 실패하였습니다. 아이디와 비밀 번호를 다시 한 번 확인해주세요.");
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response refreshTokenNotFoundException() {
        return Response.failure(404, "입력하신 정보와 일치하는 재발급 토큰이 존재하지 않습니다.");
    }
}
