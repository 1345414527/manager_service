package top.codekiller.manager.examination.service.interfaces.statistic;

import com.sun.org.apache.xpath.internal.operations.Bool;
import top.codekiller.manager.examination.pojo.Test;
import top.codekiller.manager.examination.pojo.data.ExamData;
import top.codekiller.manager.examination.pojo.data.ExamDate;
import top.codekiller.manager.examination.pojo.data.ExamSubjectProportion;
import top.codekiller.manager.examination.pojo.data.UserScore;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/13 10:53
 * @Description 考试数据服务接口
 */
public interface IExamDataService {

    /**
    * @Description 查询用户的考试分数相关信息
    * @date 2020/7/13 11:09
    * @param subjectIdStr
    * @param userIdStr
    * @return top.codekiller.manager.examination.pojo.data.UserScore
    */
    UserScore queryUserExamScore(String subjectIdStr, String userIdStr,Integer dataNum);


    /**
    * @Description 查询所有的考试分数相关信息
    * @date 2020/7/15 21:11
    * @param subjectIdStr
    * @return top.codekiller.manager.examination.pojo.data.UserScore
    */
    UserScore queryUserExamScore(String subjectIdStr,Integer dataNum);

    /**
    * @Description 根据试卷试卷的id查询平均分
    * @date 2020/7/13 11:50
    * @param testId
    * @return java.lang.Double
    */
    Double queryAverScoreByTestId(String testId);

    /**
    * @Description 计算日期数据
    * @date 2020/7/14 1:03
    * @param userId
    * @param userScore
    * @return java.lang.Boolean
    */
    Boolean calculateExamDate(Long userId,UserScore userScore);


    /**
    * @Description 计算考试的学科数
    * @date 2020/7/14 9:48
    * @param test
    * @param userScore
    * @return java.lang.Boolean
    */
    Boolean calculateExamSubjectNum(Test test,UserScore userScore);

    /**
     * @Description 计算考试的学科比例
     * @date 2020/7/14 9:49
     * @param examSubjectProportions
     * @param totalNum
     * @return java.lang.Boolean
     */
     Boolean calculateExamSubjectProportion(List<ExamSubjectProportion> examSubjectProportions, Integer totalNum);


     /**
     * @Description 剔除重复数据
     * @date 2020/7/15 22:13
     * @param data
     * @return void
     */
     void eliminateDuplicateData(List<ExamData> data);
}
