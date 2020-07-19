package top.codekiller.manager.examination.service.interfaces.exam;

import top.codekiller.manager.examination.pojo.ExamResult;

/**
 * @author codekiller
 * @date 2020/7/8 9:27
 * @Description 考试结果的接口
 */
public interface IExamResultService {

    /**
     * @Description 根据用户订阅试卷的id查询试卷的结果
     * @date 2020/7/7 18:09
     * @param subscribeExamId
     * @return top.codekiller.manager.examination.pojo.ExamResult
     */
    ExamResult queryAllExamResult(Long subscribeExamId);
}
