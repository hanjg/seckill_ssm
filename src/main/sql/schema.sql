# SET GLOBAL sql_mode = '';

CREATE TABLE seckill (
  seckill_id  BIGINT       NOT NULL AUTO_INCREMENT
  COMMENT '商品ID',
  name        VARCHAR(120) NOT NULL
  COMMENT '商品名称',
  number      INT          NOT NULL
  COMMENT '商品数量',
  start_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '秒杀开始时间',
  end_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '秒杀结束时间',
  create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time)
    COMMENT '索引',
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
  COMMENT = '秒杀库存表';

INSERT INTO seckill (name, number, start_time, end_time) VALUES
  ('1000元秒杀iphone10', 100, '2018-01-01 00:00:00', '2018-01-02 00:00:00'),
  ('900元秒杀iphone9', 200, '2018-01-01 00:00:00', '2018-01-02 00:00:00'),
  ('800元秒杀iphone8', 300, '2019-01-01 00:00:00', '2019-01-02 00:00:00'),
  ('700元秒杀iphone7', 400, '2018-01-01 00:00:00', '2019-01-01 00:00:00'),
  ('600元秒杀iphone6', 500, '2018-01-01 00:00:00', '2019-01-01 00:00:00'),
  ('500元秒杀iphone5', 600, '2018-01-01 00:00:00', '2019-01-01 00:00:00'),
  ('400元秒杀iphone4', 700, '2018-01-01 00:00:00', '2019-01-01 00:00:00'),
  ('300元秒杀iphone3', 800, '2018-01-01 00:00:00', '2019-01-01 00:00:00'),
  ('200元秒杀iphone2', 900, '2018-01-01 00:00:00', '2019-01-01 00:00:00'),
  ('100元秒杀iphone1', 1000, '2018-01-01 00:00:00', '2019-01-01 00:00:00');

CREATE TABLE success_killed (
  seckill_id  BIGINT    NOT NULL
  COMMENT '商品ID',
  user_phone  BIGINT    NOT NULL
  COMMENT '用户手机号',
  state       TINYINT   NOT NULL DEFAULT -1
  COMMENT '状态',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  PRIMARY KEY (seckill_id, user_phone),
  KEY idx_create_time(create_time)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8
  COMMENT = '秒杀成功明细表';

