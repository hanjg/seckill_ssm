package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.seckill.service.impl.SeckillServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: hjg
 * @Date: Create in 2018/1/30 21:46
 * @Description:
 */
public class RedisDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDao.class);

    private final JedisPool jedisPool;
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, int port) {
        this.jedisPool = new JedisPool(ip, port);
    }

    /**
     * 反序列化，获得redis中的对象
     */
    public Seckill getSeckill(long seckillId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckillId;
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null) {
                Seckill seckill = schema.newMessage();
                //反序列化对象
                ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                return seckill;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            closeJedis(jedis);
        }
        return null;
    }

    /**
     * 序列化对象，向redis中存放对象
     */
    public String putSeckill(Seckill seckill) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckill.getSeckillId();
            byte[] bytes = ProtostuffIOUtil
                    .toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //缓存超时，单位s
            int timeout = 60 * 60;
            String result = jedis.setex(key.getBytes(), timeout, bytes);
            return result;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            closeJedis(jedis);
        }
        return null;
    }

    private void closeJedis(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}
