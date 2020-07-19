package top.codekiller.manager.examination.enums;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * @author codekiller
 * @date 2020/6/16 23:34
 * @description 已发布试卷的状态枚举
 */

@AllArgsConstructor
@NoArgsConstructor
public enum PublicTestStatus {


    /**
     * 已删除
     */
    DELETED(-2,"已删除"),


    /**
     * 初始化
     */
    INIT(-1,"初始化"),

    /**
     * 未开启
     */
    UNOPENED(0,"未开启"),

    /**
     * 正在进行
     */
    PROCESSING(1,"正在进行"),

    /**
     * 已结束
     */
    CLOSED(2,"已结束");



    /**
     * 试卷状态
     */
    private Integer value;

    /**
     * 描述信息
     */
    private String desc;




    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
