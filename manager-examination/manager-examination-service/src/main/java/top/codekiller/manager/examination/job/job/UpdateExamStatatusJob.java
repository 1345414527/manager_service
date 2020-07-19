package top.codekiller.manager.examination.job.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.examination.enums.ExamStatus;
import top.codekiller.manager.examination.pojo.SubscribeExam;
import top.codekiller.manager.examination.service.interfaces.exam.IExamAnswerService;
import top.codekiller.manager.examination.utils.DateTimeUtils;

import java.time.Instant;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/6 17:19
 * @Description 定时考试状态任务
 */
@Component
@Slf4j
public class UpdateExamStatatusJob extends QuartzJobBean {

    @Autowired
    private IExamAnswerService examAnswerService;


    /**
    * @Description 更新答题的状态
    * @date 2020/7/6 22:50
    * @param jobExecutionContext
    * @return void
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<SubscribeExam> subscribeExams = this.examAnswerService.queryExamInfoByStatus(ExamStatus.ANSWERING_EXAM.getValue());
        log.info("更新答题的状态任务正在执行中...");
        subscribeExams.forEach((exam)->{
            if(Instant.now().toEpochMilli()>=DateTimeUtils.convertLocalDateTime2Millis(exam.getFinishWorkTime())){
                //更新状态
                Integer result = this.examAnswerService.updateExamStatusById(exam.getId(), ExamStatus.ANSWERED_EXAM.getValue(), exam.getVersion());
                if(result==1){
                    log.info("{} 答题的状态更新为{} ",exam.getId(),ExamStatus.ANSWERED_EXAM.getValue());
                }
            }
        });

    }
}
