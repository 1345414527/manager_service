package top.codekiller.manager.examination.constant;

/**
 * @author codekiller
 * @date 2020/7/15 22:25
 * @Description redis的常量
 */
public abstract class RedisConstant {

    /**
     * 用户考试统计数据存放在redis中的名字常量(真正的名字是常量+userId)
     */
    public static final String STATISTIC_EXAM_DATA_NAME="user:statistics:ExamData:";

    /**
     * 所有用户考试统计数据存放在redis中的名字常量
     */
    public static final String STATISTIC_ALL_EXAM_DATA_NAME="user:statistics:allExamData";

    /**
     * 过期时间(小时)
     */
    public static final Integer STATISTIC_DATA_TIMEOUT=24;
}
