package vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 通用API响应结果封装
 * @param <T> 响应数据的类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务状态码
     * 200: 成功
     * 500: 系统内部错误
     * 其他: 自定义业务错误码
     */
    private int code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 全局链路ID
     */
    private String traceId;


    // --- 构造方法 ---

    public Result() {}

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message, String traceId) {
        this.code = code;
        this.message = message;
        this.traceId = traceId;
    }

    // --- 静态工厂方法 (推荐在Controller中使用) ---

    /**
     * 操作成功（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功");
    }

    /**
     * 操作成功（带数据）
     */
    public static <T> Result<T> success( T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 操作成功（自定义消息 + 数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 操作失败（默认500）
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }

    /**
     * 操作失败（自定义状态码 + 消息）
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 操作失败（自定义状态码 + 消息 + 可追踪全局链条ID）
     */
    public static <T> Result<T> error(int code, String message, String traceId) {
        return new Result<>(code, message, traceId);
    }


}