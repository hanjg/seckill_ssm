UPDATE seckill
SET number = number - 1
WHERE seckill_id = 1000
      AND number > 0;


SET @r_result = -3;
CALL execute_seckill(1003, 1111111111, now(), @r_result);
SELECT @r_result;