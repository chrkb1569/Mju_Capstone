package mju.capstone.project.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response {
    private int code;
    private boolean success;
    private Result result;

    public static Response success() {
        return new Response(0, true, null);
    }

    public static <T> Response success(T data) {
        return new Response(0, true, new Success(data));
    }

    public static <T> Response failure(int code, String message) {
        return new Response(code, false, new Failure(message));
    }
}
