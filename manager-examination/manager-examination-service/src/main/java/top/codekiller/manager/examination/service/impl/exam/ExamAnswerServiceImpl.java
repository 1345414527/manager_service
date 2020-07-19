package top.codekiller.manager.examination.service.impl.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.codekiller.manager.examination.enums.ExamStatus;
import top.codekiller.manager.examination.enums.TopicAnswerStatus;
import top.codekiller.manager.examination.interceptor.ExamInterceptor;
import top.codekiller.manager.examination.mapper.ExamAnswerSituationMapper;
import top.codekiller.manager.examination.mapper.PublicTestMapper;
import top.codekiller.manager.examination.mapper.SubscribeExamMapper;
import top.codekiller.manager.examination.pojo.*;
import top.codekiller.manager.examination.pojo.log.ExamLog;
import top.codekiller.manager.examination.service.interfaces.exam.IExamAnswerService;
import top.codekiller.manager.examination.utils.DateTimeUtils;
import top.codekiller.manager.examination.utils.MongoLogUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author codekiller
 * @date 2020/7/6 10:15
 * @Description 用户试卷答题操作服务接口的实现
 */
@Service
@Slf4j
public class ExamAnswerServiceImpl implements IExamAnswerService {

    @Autowired
    private SubscribeExamMapper subscribeExamMapper;

    @Autowired
    private PublicTestMapper publicTestMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ExamAnswerSituationMapper examAnswerSituationMapper;

    /**
     * @Description 根据用户的订阅id设置开始和结束考试的时间
     * @date 2020/7/6 14:59
     * @param id
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object>  updateAllWorkTimeById(Long id){


        //获取当前时间的毫秒数
        long currentTimeMilli = Instant.now().toEpochMilli();

        //先查询数据
        SubscribeExam subscribeExam = this.subscribeExamMapper.selectById(id);
        PublicTest publicTest = this.publicTestMapper.selectOne(new QueryWrapper<PublicTest>().lambda().eq(PublicTest::getTestId, subscribeExam.getTestId()));

        //时间比较，看能否进行更新时间
        if(DateTimeUtils.convertLocalDateTime2Millis(publicTest.getStartTime()) <=currentTimeMilli&&
           DateTimeUtils.convertLocalDateTime2Millis(publicTest.getEndTime())>=currentTimeMilli){
            //获取开始时间
            LocalDateTime currentTime=DateTimeUtils.convertMillis2LocalDateTime(currentTimeMilli);

            //计算结束时间
            //先查询试卷信息
            Test test = this.mongoTemplate.findOne(new Query(Criteria.where("id").is(subscribeExam.getTestId())), Test.class);
            //获取限时时间和试卷结束时间
            Integer astrict = test.getAstrict();
            LocalDateTime testEndTime = publicTest.getEndTime();

            //根据开始时间和试卷限时计算理论结束时间
            LocalDateTime theoreticalFinishTime = DateTimeUtils.calculateLocalDateTimeWithMinute(currentTime, (long)astrict);

            //获取结束时间和理论结束时间中小的那个
            LocalDateTime actuallyFinishTime=DateTimeUtils.getEarlierLocalDateTime(testEndTime,theoreticalFinishTime);

            //数据库更新
            Integer result = this.subscribeExamMapper.updateAllWorkTimeById(id, currentTime,actuallyFinishTime,ExamStatus.ANSWERING_EXAM.getValue(),subscribeExam.getVersion());

            //如果修改成功，返回开始时间
            if(result==1){
                DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                //存储数据
                Map<String,Object> resultDate=new HashMap<>(16);
                resultDate.put("beginWorkTime",formatter.format(currentTime));
                resultDate.put("finishWorkTime",formatter.format(actuallyFinishTime));
                return resultDate;
            }
            return null;
        }
        return null;
    }

    /**
     * @Description 根据id修改考试状态
     * @date 2020/7/6 17:21
     * @param id
     * @param status
     * @return java.lang.Integer
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateExamStatusById(Long id, Integer status,Long version) {
        Integer result=this.subscribeExamMapper.updateExamStatusById(id,status,version);
        return result;
    }

    /**
    * @Description 通过状态信息查询订阅信息
    * @date 2020/7/6 22:33
    * @param status
    * @return java.util.List<top.codekiller.manager.examination.pojo.SubscribeExam>
    */
    @Override
    public List<SubscribeExam> queryExamInfoByStatus(Integer status) {
        return this.subscribeExamMapper.queryExamInfoByStatus(status);
    }

    /**
    * @Description 试卷提交
    * @date 2020/7/7 13:14
    * @param examAnswerSituations
    * @return java.lang.Integer
    */
    @Override
    public Integer commitExam(List<ExamAnswerSituation> examAnswerSituations) {

        if(CollectionUtils.isEmpty(examAnswerSituations)){
            return 0;
        }

        //先获取订阅的试卷信息
        SubscribeExam subscribeExam = this.subscribeExamMapper.selectById(examAnswerSituations.get(0).getSubscribeExamId());

        //看看有没有提交
        if(subscribeExam.getStatus().equals(ExamStatus.ANSWERED_EXAM.getValue())){
            return 0;
        }


        //获取试卷信息
        Test test = this.mongoTemplate.findOne(new Query(Criteria.where("id").is(subscribeExam.getTestId())), Test.class);


        Integer result=0;

        //判断是覆盖成绩还是添加成绩
        if(subscribeExam.getStatus().equals(ExamStatus.AGAIN_EXAM.getValue())||
            subscribeExam.getFrequency()>0){
            result= this.coverCommit(examAnswerSituations, subscribeExam,test);

            //日志记录
            MongoLogUtils.insertExamLog("提交试卷，并覆盖了成绩。第"+subscribeExam.getFrequency()+"次提交");
            return result;
        }else{
            result=this.commit(examAnswerSituations,subscribeExam,test);

            //日志记录
            MongoLogUtils.insertExamLog("提交试卷，第1次提交");
            return result;
        }
    }

    /**
    * @Description 提交试卷
    * @date 2020/7/12 14:25
    * @param examAnswerSituations
    * @param subscribeExam
    * @param test
    * @return java.lang.Integer
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer commit(List<ExamAnswerSituation> examAnswerSituations,SubscribeExam subscribeExam,Test test) {
        double totalScore=0;
        Integer result=0;

        //遍历试题
        for (ExamAnswerSituation examAnswerSituation : examAnswerSituations) {
            if(examAnswerSituation.getUserAnswer()=="-1"){
                examAnswerSituation.setAnswerSituation(TopicAnswerStatus.ANSWER_NULL.getValue());
                examAnswerSituation.setScore(0);
            }else {
                Topic topic = this.mongoTemplate.findOne(new Query(Criteria.where("id").is(examAnswerSituation.getTopicId())), Topic.class);
                if (StringUtils.equals(examAnswerSituation.getUserAnswer(), topic.getAnswer())) {
                    double score = topic.getType() == 0 ? test.getSelectScore() : test.getJudgeScore();
                    examAnswerSituation.setAnswerSituation(TopicAnswerStatus.ANSWER_RIGHT.getValue());
                    examAnswerSituation.setScore(score);
                    totalScore += score;
                } else {
                    examAnswerSituation.setAnswerSituation(TopicAnswerStatus.ANSWER_ERROR.getValue());
                    examAnswerSituation.setScore(0);
                }
            }
            examAnswerSituation.setCreated(new Date());
            examAnswerSituation.setId(null);

            //插入数据
            result = this.examAnswerSituationMapper.insert(examAnswerSituation);
            if(result!=1){
                return 0;
            }
        }


        //更新试卷的总分,状态和时间
        result=this.subscribeExamMapper.updateScoreAndStatus(totalScore, ExamStatus.ANSWERED_EXAM.getValue(),
                subscribeExam.getId(), subscribeExam.getVersion(),
                LocalDateTime.now() ,subscribeExam.getFrequency()+1);

        if(result==1){
            log.info("用户 {} 成功提交试卷 {},分数为 {}",subscribeExam.getUserId(),subscribeExam.getTestId(),totalScore);
        }

        return result;
    }

    /**
    * @Description 对成绩进行一个覆盖
    * @date 2020/7/12 14:19
    * @param examAnswerSituations
    * @param subscribeExam
    * @param test
    * @return java.lang.Integer
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer coverCommit(List<ExamAnswerSituation> examAnswerSituations,SubscribeExam subscribeExam,Test test){
        double totalScore=0;
        Integer result=0;

        //遍历试题
        for (ExamAnswerSituation examAnswerSituation : examAnswerSituations) {
            if(examAnswerSituation.getUserAnswer()=="-1"){
                examAnswerSituation.setAnswerSituation(TopicAnswerStatus.ANSWER_NULL.getValue());
                examAnswerSituation.setScore(0);
            }else {
                Topic topic = this.mongoTemplate.findOne(new Query(Criteria.where("id").is(examAnswerSituation.getTopicId())), Topic.class);
                if (StringUtils.equals(examAnswerSituation.getUserAnswer(), topic.getAnswer())) {
                    double score = topic.getType() == 0 ? test.getSelectScore() : test.getJudgeScore();
                    examAnswerSituation.setAnswerSituation(TopicAnswerStatus.ANSWER_RIGHT.getValue());
                    examAnswerSituation.setScore(score);
                    totalScore += score;
                } else {
                    examAnswerSituation.setAnswerSituation(TopicAnswerStatus.ANSWER_ERROR.getValue());
                    examAnswerSituation.setScore(0);
                }
            }

            //修改数据
            result = this.examAnswerSituationMapper.update(examAnswerSituation,new QueryWrapper<ExamAnswerSituation>().lambda()
                                                                                .eq(ExamAnswerSituation::getTopicId,examAnswerSituation.getTopicId())
                                                                                .eq(ExamAnswerSituation::getSubscribeExamId,subscribeExam.getId()));
            if(result==0){
                return 0;
            }
        }

        //更新试卷的总分,状态和时间
        result=this.subscribeExamMapper.updateScoreAndStatus(totalScore, ExamStatus.ANSWERED_EXAM.getValue(),
                subscribeExam.getId(), subscribeExam.getVersion(),
                LocalDateTime.now() ,subscribeExam.getFrequency()+1);

        if(result==1){
            log.info("用户 {} 成功提交试卷 {},分数为 {} (这是第{}此提交)",subscribeExam.getUserId(),subscribeExam.getTestId(),totalScore,subscribeExam.getFrequency());
        }

        return result;
    }


}
