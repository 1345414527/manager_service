package top.codekiller.manager.examination.job.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.codekiller.manager.examination.job.job.UpdateExamStatatusJob;

/**
 * @author codekiller
 * @date 2020/7/6 22:52
 * @Description 定时更新状态（周期性的定时任务）
 */
@Configuration
public class UpdateExamStatusConfig {

    /**
    * @Description 定时的更新用户考试的状态
    * @date 2020/7/6 22:56
    * @return org.quartz.JobDetail
    */
    @Bean
    public JobDetail UpdateExamStatusConfigJobDetail(){
        return JobBuilder.newJob(UpdateExamStatatusJob.class).withIdentity("updateExamStatus","subscribe_exam_service")
                                                                .withDescription("定时的更新用户考试的状态信息")
                                                                .storeDurably()
                                                                .build();
    }


    /**
    * @Description 定时的更新用户考试的状态触发器
    * @date 2020/7/6 23:03
    * @return org.quartz.Trigger
    */
    @Bean
    public Trigger UpdateExamStatusConfigTrigger(){
        return TriggerBuilder.newTrigger().withIdentity("UpdateExamStatusConfigTrigger","subscribe_exam_service")
                                            .withDescription("定时的更新用户考试的状态触发器")
                                            .forJob(this.UpdateExamStatusConfigJobDetail())
                                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ? "))
                                            .startNow()
                                            .build();
    }
}
