package org.seckill.exception;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/27 20:12
 * @Description:
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
