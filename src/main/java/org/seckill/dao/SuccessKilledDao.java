package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/26 12:54
 * @Description: 秒杀明细,使用存储过程管理事务的时候无须使用。
 */
public interface SuccessKilledDao {

    /**
     * 插入购买明细，可过滤重复
     *
     * @return 返回影响的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id,userPhone查询SuccessKilled并携带秒杀商品对象实体
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
