package top.codekiller.manager.examination.job.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.codekiller.manager.examination.job.job.UpdatePublicTestStatusJob;

/**
 * @author codekiller
 * @date 2020/6/17 0:10
 * @description 定时更新状态（周期性的定时任务）
 */
@Configuration
public class UpdatePublicTestStatusConfig {



     /**
     * @Description 定时的更新试卷的状态的任务
     * @date 2020/6/18 18:53
     * @return org.quartz.JobDetail
     */
    @Bean
    public JobDetail updatePublicTestStatusJobDetail(){
        return JobBuilder.newJob(UpdatePublicTestStatusJob.class).withIdentity("updatePublicTestStatus","manager_exam_service")
                                                                 .withDescription("定时更新发布试卷状态")
                                                                 .storeDurably()
                                                                 .build();
    }

    /**
    * @Description 定时的更新试卷的状态的触发器
    * @date 2020/6/18 20:53
    * @return org.quartz.Trigger
    */
    @Bean
    public Trigger UpdatePublicTestStatusTrigger(){
        return TriggerBuilder.newTrigger()
                            .withIdentity("UpdatePublicTestStatus","manager_exam_service")
                            .withDescription("定时更新发布试卷状态")
                            .forJob(this.updatePublicTestStatusJobDetail())
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ? "))
                            .startNow()
                            .build();
    }
}
