package top.codekiller.manager.examination.service.impl.examinee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.examination.enums.ExamStatus;
import top.codekiller.manager.examination.interceptor.ExamInterceptor;
import top.codekiller.manager.examination.mapper.ExamAnswerSituationMapper;
import top.codekiller.manager.examination.mapper.SubscribeExamMapper;
import top.codekiller.manager.examination.pojo.ExamAnswerSituation;
import top.codekiller.manager.examination.pojo.SubscribeExam;
import top.codekiller.manager.examination.pojo.log.ExamLog;
import top.codekiller.manager.examination.service.interfaces.examinee.IExamineeService;
import top.codekiller.manager.examination.utils.MongoLogUtils;
import top.codekiller.manager.examination.utils.MongodbUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author codekiller
 * @date 2020/7/8 10:43
 * @Description 考生操作服务接口实现
 */
@Service
public class ExamineeServiceImpl implements IExamineeService {

    @Autowired
    private SubscribeExamMapper subscribeExamMapper;

    @Autowired
    private ExamAnswerSituationMapper examAnswerSituationMapper;


    /**
     * @Description 重置考生的试卷(连着分数也一起重置)
     * @date 2020/7/12 13:27
     * @param examId
     * @return java.lang.Integer
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer reset(long examId) {
        SubscribeExam subscribeExam = this.subscribeExamMapper.selectById(examId);
        if(subscribeExam==null){
            return null;
        }


        //获取初始化时间
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime initTime= LocalDateTime.parse("2020-01-01 00:00:00",formatter);

        //修改答题信息
        subscribeExam.setStatus(ExamStatus.UNANSWERED_EXAM.getValue());
        subscribeExam.setBeginWorkTime(initTime);
        subscribeExam.setFinishWorkTime(initTime);
        subscribeExam.setScore(0);
        subscribeExam.setFrequency(0);
        this.subscribeExamMapper.updateById(subscribeExam);

        //删除答题的信息
        this.examAnswerSituationMapper.delete(new QueryWrapper<ExamAnswerSituation>().lambda().eq(ExamAnswerSituation::getSubscribeExamId,subscribeExam.getId()));

        //日志记录
        MongoLogUtils.insertExamLog(ExamInterceptor.getUserInfo().getUsername()+"重置考生"+subscribeExam.getUserId()+"试卷"+subscribeExam.getTestId()+"，并将考试状态置位未考试,考试次数置为0");

        return ExamStatus.UNANSWERED_EXAM.getValue();
    }


    /**
    * @Description 重置考生的试卷(考生的分数和每个试题的分数不会改变，再一次做的时候是覆盖分数)
    * @date 2020/7/12 14:09
    * @param examId
    * @return java.lang.Integer
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer again(long examId) {
        SubscribeExam subscribeExam = this.subscribeExamMapper.selectById(examId);
        if(subscribeExam==null){
            return null;
        }

        //只有当前的考试为已答题才能重置
        if(!subscribeExam.getStatus().equals(ExamStatus.ANSWERED_EXAM.getValue())){
            return null;
        }

        //获取初始化时间
//        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime initTime= LocalDateTime.parse("2020-01-01 00:00:00",formatter);

//        subscribeExam.setBeginWorkTime(initTime);
//        subscribeExam.setFinishWorkTime(initTime);
        subscribeExam.setStatus(ExamStatus.AGAIN_EXAM.getValue());
        this.subscribeExamMapper.updateById(subscribeExam);

        //日志记录
        MongoLogUtils.insertExamLog(ExamInterceptor.getUserInfo().getUsername()+"重置考生"+subscribeExam.getUserId()+"试卷"+subscribeExam.getTestId()+"，并将考试状态设为再次考试");

        return ExamStatus.AGAIN_EXAM.getValue();
    }
}
