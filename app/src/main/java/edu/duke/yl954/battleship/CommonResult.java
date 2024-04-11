package edu.duke.yl954.battleship;

public class CommonResult<T> {

    private long code;

    private String message;

    private T data;

    protected CommonResult() {}

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(200, "Operation Success", data);
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(500, message, null);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
