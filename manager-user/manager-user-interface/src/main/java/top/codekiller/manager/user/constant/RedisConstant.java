package top.codekiller.manager.user.constant;

/**
 * @author codekiller
 * @date 2020/7/14 23:21
 * @Description redis的常量
 */
public abstract class RedisConstant {

    /**
     * 用户统计数据存放在redis中的名字常量
     */
    public static final String STATISTIC_DATA_NAME="user:statistics:userData";

    /**
     * 过期时间(小时)
     */
    public static final Integer STATISTIC_DATA_TIMEOUT=24;
}
