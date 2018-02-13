package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/26 12:02
 * @Description: 秒杀商品库存
 */
public interface SeckillDao {


    /**
     * 根据id查询秒杀商品对象
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量分页查询商品列表
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 商品的总数量
     */
    int getTotalNumber();

    /**
     * 使用存储过程执行秒杀
     */
    void killByProcedure(Map<String, Object> paramMap);

    /**
     * 减库存
     *
     * @return 返回影响的行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 增加商品对象
     */
    int insertOne(Seckill seckill);

    /**
     * 删除商品对象
     */
    int deleteOne(long seckillId);

    /**
     * 更新商品对象
     */
    int update(@Param("seckillId") long seckillId, @Param("seckill") Seckill seckill);
}
