# 将语句的结束符设为$$
DELIMITER $$
# 定义存储过程
CREATE PROCEDURE seckill.execute_seckill(
  IN  v_seckill_id BIGINT,
  IN  v_phone      BIGINT,
  IN  v_kill_time  TIMESTAMP,
  OUT r_result     INT
)
  BEGIN
    DECLARE infect_count INT DEFAULT 0;
    START TRANSACTION;
    INSERT IGNORE INTO success_killed (seckill_id, user_phone, create_time)
    VALUES (v_seckill_id, v_phone, v_kill_time);
    #     row_count()返回上一条修改类型sql的影响行数。0：未修改数据；>0:修改行数；<0:sql错误/未执行修改sql。
    SELECT row_count()
    INTO infect_count;
    IF (infect_count = 0)
    THEN
      ROLLBACK;
      SET r_result = -1;
    ELSEIF (infect_count < 0)
      THEN
        ROLLBACK;
        SET r_result = -2;
    ELSE
      UPDATE seckill
      SET number = number - 1
      WHERE seckill_id = v_seckill_id
            AND v_kill_time > seckill.start_time
            AND v_kill_time < seckill.end_time
            AND number > 0;
      SELECT row_count()
      INTO r_result;
      IF (infect_count = 0)
      THEN
        ROLLBACK;
        SET r_result = 0;
      ELSEIF (infect_count < 0)
        THEN
          ROLLBACK;
          SET r_result = -2;
      ELSE
        COMMIT;
        SET r_result = 1;
      END IF;
    END IF;
  END $$

DELIMITER ;

