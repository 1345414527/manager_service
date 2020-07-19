package top.codekiller.manager.examination.service.impl.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.common.utils.JsonUtils;
import top.codekiller.manager.examination.client.UserClient;
import top.codekiller.manager.examination.interceptor.ExamInterceptor;
import top.codekiller.manager.examination.mapper.SubscribeExamMapper;
import top.codekiller.manager.examination.pojo.PublicTest;
import top.codekiller.manager.examination.pojo.SubscribeExam;
import top.codekiller.manager.examination.pojo.Test;
import top.codekiller.manager.examination.pojo.bo.SubscribeExamBo;
import top.codekiller.manager.examination.service.interfaces.test.IPublicTestService;
import top.codekiller.manager.examination.service.interfaces.exam.ISubscribeExamService;
import top.codekiller.manager.examination.service.interfaces.test.ITestService;
import top.codekiller.manager.examination.utils.MongoLogUtils;
import top.codekiller.manager.examination.utils.MongodbUtils;
import top.codekiller.manager.user.pojo.User;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author codekiller
 * @date 2020/6/17 23:26
 * @description 用户订阅试卷操作服务接口实现类
 */
@Service
@Slf4j
public class SubscribeExamServiceImpl implements ISubscribeExamService {


    @Autowired
    private SubscribeExamMapper subscribeExamMapper;

    @Autowired
    private ITestService testService;

    @Autowired
    private IPublicTestService publicTestService;

    @Autowired
    private UserClient userClient;


    /**
     * 插入用户订阅试卷记录
     * @param subscribeExam
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertSubscribeExamInfo(SubscribeExam subscribeExam) {
        //查询当前订阅的数量
        Integer count = this.subscribeExamMapper.selectCount(new QueryWrapper<SubscribeExam>().lambda().eq(SubscribeExam::getUserId, subscribeExam.getUserId())
                                                                                                        .eq(SubscribeExam::getTestId, subscribeExam.getTestId()));
       //当前订阅信息不出在时，才新增一条
        if(count==0) {
            subscribeExam.setCreated(new Date());
            subscribeExam.setId(null);
            MongoLogUtils.insertExamLog("新增了一个考试"+subscribeExam.getTestId()+"的订阅");
            return this.subscribeExamMapper.insert(subscribeExam);
        }
        return 0;
    }


    /**
     * 根据用户id查询试卷订阅信息
     * @param userId
     * @return
     */
    @Override
    public List<SubscribeExam> querySubscribeTestIdAndDeletedByUserId(Long userId){
        List<SubscribeExam> subscribeExams = this.subscribeExamMapper.querySubscribeTestIdAndDeletedByUserId(userId);
        return subscribeExams;
    }

    /**
     * 根据条件查询订阅的试卷
     * @param key     关键字(包含名称，备注，学校，出题人)
     * @param page    页码数
     * @param row     页行数
     * @param status 订阅记录的状态0:未考试,1:正在考试,2:已考试
     * @param deleted 试卷的订阅状态,0:未删除,1:已删除
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @param userId
     * @return
     */
    @Override
    public PageResult<SubscribeExamBo> queryAllExamInfo(String key, Integer page, Integer row, Integer deleted,String subject, Integer status, String orderField, Integer order,Long userId) {
        if(userId==null){
            System.out.println(ExamInterceptor.THREAD_LOCAL.get());
            //获取用户信息，判断是否为管理员
            Map<String, Object> result = this.userClient.queryUserInfoByUsername(ExamInterceptor.THREAD_LOCAL.get().getUsername());
            String userstr = JsonUtils.serialize(result.get("user"));
            User user = JsonUtils.parse(userstr, User.class);
            if(!user.getStatus()){
                return null;
            }
        }

        //获取排序字段名称
        orderField=judgeOrderField(orderField);

        //查询相关考试信息
        List<SubscribeExamBo> subscribeExamBos = this.subscribeExamMapper.querySubscribeExamBoInfo(status, deleted, userId, orderField, order == 0 ? "asc" : "desc");

        //查询试卷信息
        List<Test> tests = this.testService.queryAllTest(key,subject);



        //总个数
        int total=0;
        //总页数
        int totalPage=0;


        //将考试数据和试卷数据进行合并
        subscribeExamBos= subscribeExamBos.stream().map(subscribeExamBo -> {
            for (int i = 0; i < tests.size(); i++) {
                if (StringUtils.equals(subscribeExamBo.getTestId(), tests.get(i).getId())) {
                    Test test = tests.get(i);
                    subscribeExamBo.init(test);
                    return subscribeExamBo;
                }

            }
            return null;
        }).collect(Collectors.toList());

        //去除结果为null的数据
        subscribeExamBos.removeIf(value->value==null);

        //获取集合大小
        total=subscribeExamBos.size();

        //获取总页数
        totalPage=(total%row==0?total/row:total/row+1);

        //获取开始值
        int offset=(page-1)*row;


        //分页查询的信息
        List<SubscribeExamBo> result=new ArrayList<>();
        if(offset<total&&(offset+row)<=total) {
            result=subscribeExamBos.subList(offset, offset + row);
        }else if (offset<total&&(offset+row)>=total){
            result=subscribeExamBos.subList(offset,total);
        }


        return new PageResult<SubscribeExamBo>((long)total,totalPage,result);

    }

    /**
     * 删除订阅信息(如果当前的考试正在进行或者已经结束了，则不能删除了)
     * @param userId
     * @param publicTestId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteSubscribe(Long userId,Long publicTestId){
        //根据id查询试卷
        PublicTest publicTest = this.publicTestService.queryStartTimeAndEndTimeById(publicTestId);

        Integer flag=0;

        //当前时间小于考试开始时间可以删除订阅
        if(publicTest!=null&&Instant.now().toEpochMilli()<Instant.from(publicTest.getStartTime().atZone(ZoneId.systemDefault())).toEpochMilli()) {
            flag=this.subscribeExamMapper.delete(new QueryWrapper<SubscribeExam>().lambda().eq(SubscribeExam::getUserId,userId)
                                                                                           .eq(SubscribeExam::getTestId,publicTest.getTestId()));
            //日志记录
            MongoLogUtils.insertExamLog("删除了考试"+publicTest.getTestId()+"的订阅信息");
        }


        return flag;
    }

    /**
     * @Description 删除订阅信息(如果当前的考试正在进行或者已经结束了，也能删除)
     * @date 2020/7/13 10:23
     * @param publicTestId
     * @return java.lang.Integer
     */
    @Override
    public Integer deleteSubscribe(Long publicTestId) {
        return  this.subscribeExamMapper.deleteById(publicTestId);
    }

    /**
     * @Description 根据排序的字段名判定查询时的字段名称
     * @date 2020/7/5 17:17
     * @param orderField  排序的字段名
     * @return java.lang.String
     */
    private  String judgeOrderField(String orderField){
        if(StringUtils.isBlank(orderField)){
            return "se.id";
        }
        switch (orderField){
            case "startTime": return "pt.start_time";
            case "endTime": return "pt.end_time";
            case "created": return "pt.created";
            case "beginWorkTime": return "se.begin_work_time";
            case "finishWorkTime": return "se.finish_work_time";
            case "score": return "se.score";
            case "frequency": return "se.frequency";
            default: return "se.id";
        }
    }


}
