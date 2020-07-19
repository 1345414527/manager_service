package top.codekiller.manager.examination.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author codekiller
 * @date 2020/7/6 15:57
 * @Description 考试的状态
 */
@AllArgsConstructor
@NoArgsConstructor
public enum  ExamStatus {


    /**
     * 未开始答题
     */
    UNANSWERED_EXAM(0,"未开始答题"),

    /**
     * 正在答题
     */
    ANSWERING_EXAM(1,"正在答题"),


    /**
     * 已答题
     */
    ANSWERED_EXAM(2,"已答题"),

    /**
     * 重新答题
     */
    AGAIN_EXAM(3,"重新答题");


    /**
     * 考试状态
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
