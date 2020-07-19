package top.codekiller.manager.examination.service.interfaces.exam;

import top.codekiller.manager.examination.pojo.ExamAnswerSituation;
import top.codekiller.manager.examination.pojo.ExamResult;
import top.codekiller.manager.examination.pojo.SubscribeExam;
import top.codekiller.manager.examination.pojo.Test;

import java.util.List;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/6 10:15
 * @Description 用户试卷答题操作服务接口
 */
public interface IExamAnswerService {


    /**
    * @Description 根据用户的订阅id设置开始和结束考试的时间
    * @date 2020/7/6 14:59
    * @param id
    * @return java.util.Map<java.lang.String,java.lang.String>
    */
    Map<String,Object>  updateAllWorkTimeById(Long id);

    /**
    * @Description 根据id修改考试状态
    * @date 2020/7/6 17:21
    * @param id
    * @param status
    * @return java.lang.Integer
    */
    Integer updateExamStatusById(Long id,Integer status,Long version);

    /**
    * @Description 通过状态信息查询订阅信息
    * @date 2020/7/6 22:22
    * @param status
    * @return java.util.List<top.codekiller.manager.examination.pojo.SubscribeExam>
    */
    List<SubscribeExam> queryExamInfoByStatus(Integer status);

    /**
    * @Description 试卷提交
    * @date 2020/7/7 13:16
    * @param examAnswerSituations
    * @return java.lang.Integer
    */
    Integer commitExam(List<ExamAnswerSituation> examAnswerSituations);

    /**
    * @Description 提交试卷
    * @date 2020/7/12 17:31
    * @param examAnswerSituations
    * @param subscribeExam
    * @param test
    * @return java.lang.Integer
    */
    Integer commit(List<ExamAnswerSituation> examAnswerSituations,SubscribeExam subscribeExam,Test test) ;

    /**
    * @Description 对成绩进行一个覆盖
    * @date 2020/7/12 17:31
    * @param examAnswerSituations
    * @param subscribeExam
    * @param test
    * @return java.lang.Integer
    */
    Integer coverCommit(List<ExamAnswerSituation> examAnswerSituations, SubscribeExam subscribeExam, Test test);


}
