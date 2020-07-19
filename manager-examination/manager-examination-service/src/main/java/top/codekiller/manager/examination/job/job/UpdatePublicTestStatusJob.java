package top.codekiller.manager.examination.job.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.examination.enums.PublicTestStatus;
import top.codekiller.manager.examination.pojo.PublicTest;
import top.codekiller.manager.examination.service.interfaces.test.IPublicTestService;
import top.codekiller.manager.examination.utils.DateTimeUtils;

import java.time.Instant;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/16 22:04
 * @description 定时更新状态任务
 */
@Component
@Slf4j
public class UpdatePublicTestStatusJob extends QuartzJobBean {

    @Autowired
    private IPublicTestService publicTestService;


    /**
     * @Description 更新发布试卷的状态
     * @date 2020/7/6 22:50
     * @param jobExecutionContext
     * @return void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<PublicTest> publicTests = this.publicTestService.queryAllPublicTests();
        log.info("更新试题的状态任务正在执行中...");

        for(PublicTest publicTest:publicTests){
            //当前时间和试卷的开始，结束时间的比较
            if(Instant.now().toEpochMilli()< DateTimeUtils.convertLocalDateTime2Millis(publicTest.getStartTime()) &&
                publicTest.getVersion()!=0){
                //当前时间小于开始时间,并且当前状态不为未开始:0时,修改当前试卷的状态为1
                if(publicTest.getStatus()!=0) {
                    if(this.publicTestService.updatePublicTestStatus(publicTest, PublicTestStatus.UNOPENED.getValue())){
                        log.info("{} 试题的状态更新为{} ",publicTest.getId(),PublicTestStatus.UNOPENED.getValue());
                    }
                }
            }else if(Instant.now().toEpochMilli()>=DateTimeUtils.convertLocalDateTime2Millis(publicTest.getStartTime())&&
                    Instant.now().toEpochMilli()<DateTimeUtils.convertLocalDateTime2Millis(publicTest.getEndTime()) &&
                    publicTest.getVersion()!=0){
                //当前时间大于等于开始时间小于结束时间,并且当前状态不为正在运行:1时，修改当前试卷的状态为1
                if(publicTest.getStatus()!=1) {
                    if(this.publicTestService.updatePublicTestStatus(publicTest, PublicTestStatus.PROCESSING.getValue())){
                        log.info("{} 试题的状态更新为{} ",publicTest.getId(),PublicTestStatus.PROCESSING.getValue());
                    }
                }
            }else{
                //当前时间大于结束时间，,并且当前状态不为已结束:2时,修改当前试卷的状态为2
                if(publicTest.getStatus()!=2&& publicTest.getVersion()!=0) {
                    if(this.publicTestService.updatePublicTestStatus(publicTest, PublicTestStatus.CLOSED.getValue())){
                        log.info("{} 试题的状态更新为{} ",publicTest.getId(),PublicTestStatus.CLOSED.getValue());
                    }
                }
            }
        }
    }


}
