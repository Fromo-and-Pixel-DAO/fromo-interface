package org.cell.froopyland_interface.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yozora
 * @date 2020/5/15
 * <p>
 * 每个操作都有自动重试策略(retryAttempts、retryInterval)
 */
@Log4j2
@Component
public final class RedisUtils {

    @Autowired
    private RedissonClient getRedissonClient;

    private static RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        redissonClient = getRedissonClient;
    }

    /**
     * 功能描述: 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间 单位:秒
     * @return boolean
     * @author yozora
     * @date 2020/5/14 22:29
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redissonClient.getKeys().expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(毫秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        long remainTimeToLive = redissonClient.getKeys().remainTimeToLive(key);
        if (remainTimeToLive == -1) {
            return -1;
        } else if (remainTimeToLive == -2) {
            return -2;
        } else {
            return remainTimeToLive / 1000L;
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键 可传入多个
     * @return >0 存在 0不存在
     */
    public static long hasKey(String... key) {
        try {
            return redissonClient.getKeys().countExists(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public static boolean delKys(String... key) {
        if (key != null && key.length > 0) {
            long delete = redissonClient.getKeys().delete(key);
            return delete != 0L;
        }
        return false;
    }

    /**
     * 功能描述: 模糊查询key
     *
     * @param pattern 正则匹配表达式
     * @param limit   查询数量
     * @return java.lang.Iterable<java.lang.String>
     * @author yozora
     * @date 18:57 2022/2/23
     **/
    public static Iterable<String> scanKeys(String pattern, int limit) {
        try {
            return redissonClient.getKeys().getKeysByPattern(pattern, limit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // ============================String=============================

    public static boolean initBucketCacheKeyListener(String key) {
        int addListener = redissonClient.getBucket(key).addListener((ExpiredObjectListener) s -> {
            System.out.println("ExpiredObjectListener: " + s);
        });
        return addListener > 0;
    }

    /**
     * String缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        if (redissonClient.getBucket(key).isExists()) {
            return (String) redissonClient.getBucket(key).get();
        } else {
            return null;
        }
    }


    /**
     * String缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(String key, Object value) {
        try {
            redissonClient.getBucket(key).set(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * String缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0将设置无限期
     * @return true成功 false 失败
     */
    public static boolean setIfAbsent(String key, Object value, long time) {
        try {
            if (time > 0) {
                redissonClient.getBucket(key).trySet(value, time, TimeUnit.SECONDS);
            } else {
                redissonClient.getBucket(key).trySet(value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * String缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redissonClient.getBucket(key).set(value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // ================================Hash=================================

    /**
     * 功能描述: 指定hash删除监听器
     *
     * @param key hash 键名
     * @return boolean
     * @author yozora
     * @date 2021/8/12 19:00
     */
    public static boolean initCacheKeyListener(String key) {
        RMap<Object, Object> redissonClientMap = redissonClient.getMap(key);
        int addListener = redissonClientMap.addListener((DeletedObjectListener) s -> {
            log.info("map object: {} removed", s);
        });
        return addListener > 0;
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hget(String key, String item) {
        return redissonClient.getMap(key).get(item);
    }


    /**
     * 获取hashKey对应的所有键
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Collection<Object> hmgetKey(String key) {
        return redissonClient.getMap(key).readAllKeySet();
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */

    public static Map<Object, Object> hmget(String key) {
        return redissonClient.getMap(key).readAllMap();
    }


    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */

    public static boolean hmset(String key, Map<String, Object> map) {
        try {
            redissonClient.getMap(key).putAll(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redissonClient.getMap(key).putAll(map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果存在将覆盖
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hsetMayCover(String key, String item, Object value) {
        try {
            redissonClient.getMap(key).put(item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果存在将覆盖
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hsetMayCover(String key, String item, Object value, long time) {
        try {
            redissonClient.getMap(key).put(item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hset(String key, String item, Object value) {
        try {
            return redissonClient.getMap(key).fastPutIfAbsent(item, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hset(String key, String item, Object value, long time) {
        try {
            redissonClient.getMap(key).fastPutIfAbsent(item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     * @return
     */


    public static long hdel(String key, Object... item) {
        return redissonClient.getMap(key).fastRemove(item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */

    public static boolean hHasKey(String key, String item) {
        return redissonClient.getMap(key).containsKey(item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */


//    public static double hincr(String key, String item, double by) {
//        redissonClient.getMap(key).addAndGet(item, by);
//        return redisTemplate.opsForHash().increment(key, item, by);
//    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
//    public static double hdecr(String key, String item, double by) {
//        return redisTemplate.opsForHash().increment(key, item, -by);
//    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> sGet(String key) {
        try {
            return redissonClient.getSet(key).readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String key, Object value) {
        try {
            return redissonClient.getSet(key).contains(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static boolean sSet(String key, Object... values) {
        try {
            return redissonClient.getSet(key).addAll(Arrays.asList(values));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */


    public static boolean sSetAndTime(String key, long time, Object... values) {
        try {
            boolean addAll = redissonClient.getSet(key).addAll(Arrays.asList(values));
            if (time > 0) {
                expire(key, time);
            }
            return addAll;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long sGetSetSize(String key) {
        try {
            return redissonClient.getSet(key).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static boolean setRemove(String key, Object... values) {
        try {
            return redissonClient.getSet(key).remove(values);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ============================sorted set=============================

    /**
     * 功能描述: 获取序列数据
     *
     * @param key       键
     * @param sortOrder 排序方式
     * @param offset    起始位置
     * @param count     数量
     * @return java.util.Collection<java.lang.Object>
     * @author yozora
     * @date 2021/8/12 14:07
     */
    public static Collection<Object> sortedGet(String key, SortOrder sortOrder, int offset, int count) {
        try {
            return redissonClient.getScoredSortedSet(key).readSort(sortOrder, offset, count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据key获取sorted set中的所有值
     *
     * @param key 键
     * @return Collection<Object>
     */
    public static Collection<Object> sortedGetAll(String key) {
        try {
            return redissonClient.getScoredSortedSet(key).readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个sorted set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sortedHasKey(String key, Object value) {
        try {
            return redissonClient.getScoredSortedSet(key).contains(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述: 序列新增对象
     *
     * @param key   键
     * @param score 积分
     * @param value 对象值
     * @return boolean
     * @author yozora
     * @date 2021/8/12 13:56
     */
    public static boolean sortedSetObject(String key, double score, Object value) {
        try {
            return redissonClient.getScoredSortedSet(key).add(score, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * description: 序列修改时间
     *
     * @param key 键
     * @return boolean success
     * @author yozora
     * @date 17:34 2022/9/15
     **/
    public static boolean sortedSetTime(String key) {
        return redissonClient.getScoredSortedSet(key).expire(Instant.now().plus(7, ChronoUnit.DAYS));
    }

    /**
     * 功能描述: 序列新增对象并设置失效时间
     *
     * @param key   键
     * @param score 积分
     * @param value 对象值
     * @return boolean
     * @author yozora
     * @date 2021/8/12 13:56
     */
    public static boolean sortedSetObjectAndTime(String key, long time, double score, Object value) {
        try {
            boolean add = redissonClient.getScoredSortedSet(key).add(score, value);
            if (time > 0) {
                expire(key, time);
            }
            return add;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 功能描述: 序列修改积分
     *
     * @param key   键
     * @param score 积分
     * @param value 对象
     * @return int 当前积分
     * @author yozora
     * @date 2021/8/12 14:00
     */
    public static int sortedSetScore(String key, double score, Object value) {
        try {
            return redissonClient.getScoredSortedSet(key).addScoreAndGetRank(value, score);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 功能描述: 获取序列分数
     *
     * @param key   键名
     * @param value 值
     * @return java.lang.Double
     * @author yozora
     * @date 2021/10/18 11:04
     */
    public static Double sortedGetScore(String key, Object value) {
        try {
            return redissonClient.getScoredSortedSet(key).getScore(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0D;
        }
    }

    /**
     * 功能描述: 获取序列大小
     *
     * @param key 键
     * @return long
     * @author yozora
     * @date 2021/8/12 14:01
     */
    public static long sortedGetSetSize(String key) {
        try {
            return redissonClient.getScoredSortedSet(key).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 功能描述: 序列移除对象
     *
     * @param key   键
     * @param value 对象
     * @return boolean
     * @author yozora
     * @date 2021/8/12 14:02
     */
    public static boolean sortedRemove(String key, Object value) {
        try {
            return redissonClient.getScoredSortedSet(key).remove(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===============================list=================================


    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public static List<Object> lGet(String key, int start, int end) {
        try {
            return redissonClient.getList(key).range(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long lGetListSize(String key) {
        try {
            return redissonClient.getList(key).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * description: 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return java.lang.Object
     * @author yozora
     * @date 18:08 2022/5/20
     **/
    public static Object lGetIndex(String key, int index) {
        try {
            return redissonClient.getList(key).get(index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * description: 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return boolean
     * @author yozora
     * @date 18:08 2022/5/20
     **/
    public static boolean lSet(String key, Object value) {

        try {
            redissonClient.getList(key).add(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * description:  将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return boolean
     * @author yozora
     * @date 18:09 2022/5/20
     **/
    public static boolean lSet(String key, Object value, long time) {
        try {
            redissonClient.getList(key).add(value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean lSet(String key, List<Object> value) {
        try {
            redissonClient.getList(key).addAll(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            redissonClient.getList(key).addAll(value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public static boolean lUpdateIndex(String key, int index, Object value) {
        try {
            redissonClient.getList(key).set(index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static boolean lRemove(String key, int count, Object value) {
        try {
            return redissonClient.getList(key).remove(value, count);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //============================Queue=======================

    /**
     * 非阻塞队列中添加数据
     *
     * @param key   队列键值
     * @param value 队列对象
     * @return
     */
    public static boolean queueAdd(String key, Object value) {
        RQueue<Object> queue = redissonClient.getQueue(key);//定义个队列
        return queue.add(value);
    }

    /**
     * 非阻塞队列中获取并删除数据
     *
     * @param key 队列键值
     * @return
     */
    public static Object queuePoll(String key) {
        RQueue<Object> queue = redissonClient.getQueue(key);//定义个队列
        return queue.poll();
    }

    /**
     * 功能描述: 获取redis锁
     *
     * @param lockKey 锁字段
     * @return boolean
     * @author yozora
     * @date 2021/8/11 14:26
     */
    public static RLock getLock(String lockKey) throws InterruptedException {
        // 尝试加锁，最多等待10秒，上锁以后10秒自动解锁
        return redissonClient.getLock(lockKey);
    }


}
