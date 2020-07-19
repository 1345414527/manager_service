package top.codekiller.manager.examination.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author codekiller
 * @date 2020/7/7 15:23
 * @Description 用户答题的情况
 */

@AllArgsConstructor
@NoArgsConstructor
public enum  TopicAnswerStatus {

    /**
     * 未答题
     */
    ANSWER_NULL(-1,"未答题"),

    /**
     * 题目答错
     */
    ANSWER_ERROR(0,"题目答错"),

    /**
     * 题目答对
     */
    ANSWER_RIGHT(1,"题目答对");


    private Integer value;

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
