package org.seckill.exception;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/27 20:12
 * @Description:
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
