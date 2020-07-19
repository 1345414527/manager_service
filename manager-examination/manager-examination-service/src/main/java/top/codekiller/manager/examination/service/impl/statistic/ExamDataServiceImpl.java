package top.codekiller.manager.examination.service.impl.statistic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.examination.constant.RedisConstant;
import top.codekiller.manager.examination.exception.CalculateExamDateException;
import top.codekiller.manager.examination.interceptor.ExamInterceptor;
import top.codekiller.manager.examination.mapper.SubscribeExamMapper;
import top.codekiller.manager.examination.pojo.Test;
import top.codekiller.manager.examination.pojo.data.ExamData;
import top.codekiller.manager.examination.pojo.data.ExamDate;
import top.codekiller.manager.examination.pojo.data.ExamSubjectProportion;
import top.codekiller.manager.examination.pojo.data.UserScore;
import top.codekiller.manager.examination.service.interfaces.statistic.IExamDataService;
import top.codekiller.manager.examination.service.interfaces.test.ITestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author codekiller
 * @date 2020/7/13 10:54
 * @Description 考试数据服务接口的实现类
 */
@Service
public class ExamDataServiceImpl implements IExamDataService {

    @Autowired
    private SubscribeExamMapper subscribeExamMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ITestService testService;

    /**
     * 计数
     */
    private static int count=0;


    /**
    * @Description 查询用户的考试分数相关信息，并放入缓存中。因为该查询操作太过于费时。为减少数据库的压力，24小时重新计算一次！
    * @date 2020/7/13 11:10
    * @param subjectIdStr
    * @param userIdStr
    * @return top.codekiller.manager.examination.pojo.data.UserScore
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserScore queryUserExamScore(String subjectIdStr, String userIdStr,Integer dataNum) {
        //查看缓存
//        String redisName=RedisConstant.STATISTIC_EXAM_DATA_NAME+ ExamInterceptor.getUserInfo().getId();
////        if(redisTemplate.hasKey(redisName)){
////            return (UserScore) this.redisTemplate.opsForValue().get(redisName);
////        }

        if(StringUtils.isBlank(subjectIdStr)||StringUtils.isBlank(userIdStr)){
            return null;
        }
        Long subjectId=Long.parseLong(subjectIdStr);
        Long userId=Long.parseLong(userIdStr);


        //查询相关信息
        List<ExamData> examDatas = this.subscribeExamMapper.queryScoreInfoByUserId(userId);

        if(dataNum==null&&!(dataNum<=20&&dataNum>=0)){
            dataNum=6;
        }

        UserScore userScore = this.calculateData(userId, subjectId, examDatas, dataNum);

//        if(!redisTemplate.hasKey(redisName)){
//            this.redisTemplate.opsForValue().set(redisName,userScore,RedisConstant.STATISTIC_DATA_TIMEOUT, TimeUnit.HOURS);
//        }

        return userScore;
    }

    /**
    * @Description 查询所有的考试分数相关信息，并放入缓存中。因为该查询操作太过于费时。为减少数据库的压力，24小时重新计算一次！
    * @date 2020/7/15 21:11
    * @param subjectIdStr
    * @return top.codekiller.manager.examination.pojo.data.UserScore
    */
    @Override
    public UserScore queryUserExamScore(String subjectIdStr,Integer dataNum) {
        //查看缓存
//        String redisName=RedisConstant.STATISTIC_ALL_EXAM_DATA_NAME;
//        if(redisTemplate.hasKey(redisName)){
//            return (UserScore) this.redisTemplate.opsForValue().get(redisName);
//        }

        if(StringUtils.isBlank(subjectIdStr)){
            return null;
        }
        Long subjectId=Long.parseLong(subjectIdStr);
        //查询相关信息
        List<ExamData> examDatas = this.subscribeExamMapper.queryScoreInfoByUserId(null);

        if(dataNum==null&&!(dataNum<=20&&dataNum>=0)){
            dataNum=20;
        }
        UserScore userScore = this.calculateData(null, subjectId, examDatas, dataNum);
        //剔除重复数据
        this.eliminateDuplicateData(userScore.getExamScores());

//        if(!redisTemplate.hasKey(redisName)){
//            this.redisTemplate.opsForValue().set(redisName,userScore,RedisConstant.STATISTIC_DATA_TIMEOUT, TimeUnit.HOURS);
//        }

        return userScore;
    }


    /**
    * @Description 计算数据
    * @date 2020/7/15 21:23
    * @param userId
    * @param subjectId
    * @param examDatas
    * @param dataNum
    * @return top.codekiller.manager.examination.pojo.data.UserScore
    */
    private UserScore calculateData(Long userId,Long subjectId,  List<ExamData> examDatas,Integer dataNum){
        UserScore userScore=new UserScore();
        userScore.setExamSubjectProportions(new ArrayList<>());
        List<ExamData> result=examDatas.stream().map(examData -> {
            Test test = this.testService.queryTestById(examData.getTestId());
            if(!this.calculateExamSubjectNum(test,userScore)){
                throw new CalculateExamDateException("计算答题数据异常");
            }

            //数据控制在我所需要的数量内
            if (test.getSubject().equals(subjectId) &&(getCount()<dataNum)){
                addCount();
                examData.setTestName(test.getName());
                examData.setAverScore(this.queryAverScoreByTestId(examData.getTestId()));
                return examData;
            }
            return null;
        }).collect(Collectors.toList());


        //清零去空
        setCount(0);
        result.removeIf(score->score==null);
        Collections.reverse(result);
        userScore.setExamScores(result);


        Boolean flag = this.calculateExamDate(userId, userScore);
        if(!flag){
            return null;
        }

        flag = this.calculateExamSubjectProportion(userScore.getExamSubjectProportions(), examDatas.size());
        if(flag){
            return userScore;
        }

        return null;
    }

    /**
    * @Description 根据试卷试卷的id查询平均分
    * @date 2020/7/13 11:50
    * @param testId
    * @return java.lang.Double
    */
    @Override
    public Double queryAverScoreByTestId(String testId) {
        return this.subscribeExamMapper.queryAverScoreByTestId(testId);
    }

    /**
    * @Description 计算日期数据
    * @date 2020/7/13 23:08
    * @param userId
    * @param userScore
    * @return java.lang.Boolean
    */
    @Override
    public Boolean calculateExamDate(Long userId,UserScore userScore) {
        if(userScore==null){
            return false;
        }

        List<LocalDateTime> dateTimes=this.subscribeExamMapper.queryFinalWorkTimeByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        userScore.setYear(year);

        //只保留当前年的数据
        dateTimes.removeIf(dateTime -> dateTime.getYear()!=year);


        userScore.init();
        List<ExamDate> examDates = userScore.getExamDates();

        dateTimes.forEach(dateTime -> {
            Integer cur=(dateTime.getMonthValue()-1)*7+dateTime.getDayOfWeek().getValue()-1;
            examDates.get(cur).examNumIncrease();
        });

        return true;
    }

    /**
     * @Description 计算考试的学科数
     * @date 2020/7/14 9:48
     * @param test
     * @param userScore
     * @return java.lang.Boolean
     */
    @Override
    public Boolean calculateExamSubjectNum(Test test, UserScore userScore) {
        if(test==null||userScore==null||userScore.getExamSubjectProportions()==null){
            return false;
        }

        Long subjectId = test.getSubject();
        List<ExamSubjectProportion> examSubjectProportions = userScore.getExamSubjectProportions();
        for (ExamSubjectProportion examSubjectProportion : examSubjectProportions) {
            if(examSubjectProportion.getSubjectId().equals(subjectId)){
                examSubjectProportion.examNumIncrease();
                examSubjectProportion.setJudgeNum(examSubjectProportion.getJudgeNum()+test.getJudge().size());
                examSubjectProportion.setSelectNum(examSubjectProportion.getSelectNum()+test.getSelect().size());
                examSubjectProportion.setTopicNum(examSubjectProportion.getTopicNum()+test.getJudge().size()+test.getSelect().size());
                return true;
            }
        }

        ExamSubjectProportion examSubjectProportion=new ExamSubjectProportion(test.getSubject(),0.0,1,
                                                                              test.getSelect().size(),test.getJudge().size(),
                                                                    test.getJudge().size()+test.getSelect().size());
        examSubjectProportions.add(examSubjectProportion);
        return true;
    }


    /**
    * @Description 计算考试的学科比例
    * @date 2020/7/14 9:49
    * @param examSubjectProportions
    * @param totalNum
    * @return java.lang.Boolean
    */
    @Override
    public Boolean calculateExamSubjectProportion(List<ExamSubjectProportion> examSubjectProportions,Integer totalNum){
        if(examSubjectProportions==null||totalNum<0){
            return false;
        }

        examSubjectProportions.forEach(examSubjectProportion -> {
            examSubjectProportion.setProportion((examSubjectProportion.getExamNum()*1.0)/totalNum);
        });

        return true;

    }

    /**
    * @Description 剔除重复数据
    * @date 2020/7/15 22:14
    * @param data
    * @return void
    */
    @Override
    public void eliminateDuplicateData(List<ExamData> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = data.size() - 1; j > i; j--) {
                if (data.get(j).getTestId().equals(data.get(i).getTestId())) {
                    data.remove(j);
                }
            }
        }
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        ExamDataServiceImpl.count = count;
    }

    private static void addCount(){
        ++ExamDataServiceImpl.count;
    }
}
