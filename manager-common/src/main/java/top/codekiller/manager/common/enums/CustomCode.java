package top.codekiller.manager.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author codekiller
 * @date 2020/5/30 12:33
 *
 * 自定义状态码
 */
@AllArgsConstructor
@NoArgsConstructor
public enum CustomCode {


    /**
     * {@code 250 TOPIC ACTION OK}.
     * 试题操作成功
     */
    TOPIC_OK(250,"TOPIC ACTION OK"),

    /**
     * {@code 251 TEST ACTION OK}.
     * 试卷操作成功
     */
    TEST_OK(251,"TEST ACTION OK"),


    /**
     * {@code 252 USER START EXAM OK}.
     * 开始考试成功
     */
    START_EXAM_OK(252,"USER START EXAM OK"),

    /**
     * {@code 253 USER FINISH EXAM OK}.
     * 结束考试成功
     */
    FINISH_EXAM_OK(253,"USER FINISH EXAM OK"),

    /**
     * {@code 254 USER COMMIT EXAM OK}.
     *用户试卷提交成功
     */
    EXAM_COMMIT_OK(254,"USER COMMIT EXAM OK"),

    /**
     * {@code 255 EXAM OPERATE OK}.
     * 试卷答题相关操作异常
     */
    EXAM_OK(255,"EXAM OPERATE OK"),

    /**
     * {@code 260 USER EXAM DATA QUERY OK}.
     * 用户的考试数据查询成功
     */
    EXAM_DATA_QUERY_OK(260,"USER EXAM DATA QUERY OK"),


    /**
     * {@code 261 USER  DATA QUERY OK}.
     * 用户的数据查询成功
     */
    USER_DATA_QUERY_OK(261,"USER DATA QUERY OK"),

    /**
     * {@code 262 EXAM LOG OPERATE OK}.
     * 日志操作成功
     */
    LOG_OK(262,"EXAM LOG OPERATE OK"),

    /**
     * {@code 263 USER INFO SEARCH OK}.
     * 用户检索成功
     */
    USER_SEARCH_OK(263,"USER INFO SEARCH OK"),


    /**
     * {@code 444 USER NOT FOUNED}.
     * 找不到用户
     */
    USER_NOT_FOUND(444,"USER NOT FOUNED"),

    /**
     * {@code 459 TOPIC OPERATE ERROR}.
     * 试题相关操作错误
     */
    TOPIC_ERROR(459,"TOPIC OPERATE ERROR"),


    /**
     * {@code 460 EXAM ANSWER OPERATE ERROR}.
     * 试卷答题相关操作异常
     */
    EXAM_ANSWER_ERROR(460,"EXAM ANSWER OPERATE ERROR"),


    /**
     * {@code 461 USER EXAM DATA QUERY ERROR}.
     * 用户的考试数据查询异常
     */
    EXAM_DATA_QUERY_ERROR(461,"USER EXAM DATA QUERY ERROR"),

    /**
     * {@code 462 USER DATA QUERY ERROR}.
     * 用户的数据查询失败
     */
    USER_DATA_QUERY_ERROR(462,"USER DATA QUERY ERROR"),

    /**
     * {@code 552 EXAHM LOG OPERATE ERROR}.
     * 日志操作失败
     */
    LOG_ERROR(522,"EXAM LOG OPERATE ERROR"),

    /**
     * {@code 555 USER INFO SEARCH ERROR}.
     * 用户检索失败
     */
    USER_SEARCH_ERROR(555,"USER INFO SEARCH ERROR");



    /**
     * 状态码
     */
    private Integer value;

    /**
     * 描述信息
     */
    private String desc;



    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
