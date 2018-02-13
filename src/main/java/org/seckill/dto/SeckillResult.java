package org.seckill.dto;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/28 14:45
 * @Description: 用于Controller，正确返回结果，错误返回错误信息
 */
public class SeckillResult<T> {

    private boolean success;
    private T result;
    private String error;

    public SeckillResult(boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
