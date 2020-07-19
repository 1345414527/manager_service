package top.codekiller.manager.examination.exception;

import top.codekiller.manager.common.exception.ApiException;
import top.codekiller.manager.common.enums.CustomCode;

/**
 * @author codekiller
 * @date 2020/7/14 10:18
 * @Description 计算考试数据异常
 */
public class CalculateExamDateException extends ApiException {

    private static final long serialVersionUID=8088898181111128629L;

    public CalculateExamDateException( String msg) {
        super(CustomCode.EXAM_DATA_QUERY_ERROR.getValue(), msg);
    }
}
