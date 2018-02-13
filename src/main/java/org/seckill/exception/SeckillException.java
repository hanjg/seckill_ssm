package org.seckill.exception;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/27 20:13
 * @Description:
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
