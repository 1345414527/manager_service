package top.codekiller.manager.examination.service.impl.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.enums.PublicTestStatus;
import top.codekiller.manager.examination.mapper.PublicTestMapper;
import top.codekiller.manager.examination.pojo.PublicTest;
import top.codekiller.manager.examination.pojo.Test;
import top.codekiller.manager.examination.pojo.bo.PublicTestBo;
import top.codekiller.manager.examination.service.interfaces.test.IPublicTestService;
import top.codekiller.manager.examination.service.interfaces.test.ITestService;
import top.codekiller.manager.examination.utils.DateTimeUtils;
import top.codekiller.manager.examination.utils.MongoLogUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/11 0:45
 * @description 公布试卷服务接口的实现类
 */
@Service
public class PublicTestServiceImpl implements IPublicTestService {

    @Autowired
    private PublicTestMapper publicTestMapper;

    @Autowired
    private ITestService testService;



    /**
     * 根据testId查询试卷发布记录
     * @param testId
     * @return
     */
    @Override
    public PublicTest queryPublicTestByTestId(String testId) {
        return  this.publicTestMapper.selectOne(new QueryWrapper<PublicTest>().lambda().eq(PublicTest::getTestId, testId));
    }

    /**
     * 根据testId删除试卷发布记录
     * @param testId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePublicTest(String testId) {
        this.publicTestMapper.deletePublicTestByTestId(testId);
    }


    /**
     * 插入testId对应的试卷发布记录
     * @param testId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertPublicTest(String testId) {
        PublicTest exist = this.queryPublicTestByTestId(testId);
        //没有记录就增加一条记录
        if(exist==null) {
            PublicTest publicTest = new PublicTest();
            publicTest.setCreated(new Date());
            publicTest.setStatus(PublicTestStatus.INIT.getValue());
            publicTest.setTestId(testId);
            this.publicTestMapper.insert(publicTest);
            MongoLogUtils.insertExamLog("发布了新的考试"+publicTest.getTestId());
        }
    }


    /**
     * 根据条件查询发布的试卷
     * @param key     关键字(包含名称，备注，学校，出题人)
     * @param page    页码数
     * @param row     页行数
     * @param status 试卷的状态,-1:初始化,0:未开始,1:开启中,2:已结束
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @return
     */
    @Override
    public PageResult<PublicTestBo> queryAllPublicTest(String key, Integer page, Integer row, String subject, Integer status, String orderField, Integer order) {

        QueryWrapper<PublicTest> wrapper = new QueryWrapper<>();

        //排序
        if(StringUtils.isNotBlank(orderField)){
            //升序
            if(order==0){
                wrapper.orderByAsc(this.judgeOrderField(orderField));
            }else if(order==1){
                //降序
                wrapper.orderByDesc(this.judgeOrderField(orderField));
            }
        }

        //状态查询
        if(status!=null){
            wrapper.eq("status",status);
        }

        //查询所有符合条件的试卷信息
        List<PublicTest> publicTests = this.publicTestMapper.selectList(wrapper);
        System.out.println("数据："+publicTests);

        //分页查询试卷信息
        List<Test> tests = this.testService.queryAllTest(key,subject);
        System.out.println("测试："+tests);

        //发送给前台的信息总集合
        List<PublicTestBo> publicTestBos=new ArrayList<>();



        //总个数
        int total=0;
        //总页数
        int totalPage=0;
        for(int i=0;i<publicTests.size();i++){
            for(int j=0;j<tests.size();j++){
                if(StringUtils.equals(publicTests.get(i).getTestId(),tests.get(j).getId())){
                    PublicTest publicTest = publicTests.get(i);
                    Test test = tests.get(j);
                    PublicTestBo publicTestBo=new PublicTestBo();
                    publicTestBo.init(publicTest,test);
                    publicTestBos.add(publicTestBo);
                    ++total;
                    break;
                }
            }
        }
        totalPage=(total%row==0?total/row:total/row+1);

        int offset=(page-1)*row;

        //分页查询的信息
        List<PublicTestBo> result=new ArrayList<>();
        if(offset<total&&(offset+row)<=total) {
            result=publicTestBos.subList(offset, offset + row);
        }else if (offset<total&&(offset+row)>=total){
            result=publicTestBos.subList(offset,total);
        }




        return new PageResult<PublicTestBo>((long)total,totalPage,result);
    }

    /**
     * 修改试卷的发布和结束时间
     * @param publicTest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePublicTestTime(PublicTest publicTest) {
        if(publicTest.getStartTime()==null||publicTest.getEndTime()==null) {
            return false;
        }

        Long version = this.publicTestMapper.queryVersionById(publicTest.getId());
        publicTest.setVersion(version+1);


        //判断当前状态
        Integer status = this.judgeStatus(Instant.now().toEpochMilli(), DateTimeUtils.convertLocalDateTime2Millis(publicTest.getStartTime()),
                                            DateTimeUtils.convertLocalDateTime2Millis(publicTest.getEndTime()));
        publicTest.setStatus(status);
        return  this.publicTestMapper.updatePublicTestTime(publicTest);
    }

    /**
     * 更新试卷的状态
     * @param publicTest
     * @param status
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePublicTestStatus(PublicTest publicTest,Integer status) {
        return   this.publicTestMapper.updatePublicTestStatusById(publicTest.getId(),status,publicTest.getVersion()+1);
    }

    /**
     * 查询已发布试卷的id，开始时间，结束时间,状态，版本
     * @return
     */
    @Override
    public List<PublicTest> queryAllPublicTests() {
        return this.publicTestMapper.queryAllPublicTests();
    }

    /**
     * 根据id查询试卷的开始时间和结束时间
     * @param id
     * @return
     */
    @Override
    public PublicTest queryStartTimeAndEndTimeById(Long id) {
        return this.publicTestMapper.queryStartTimeAndEndTimeById(id);
    }


    /**
    * @Description 根据排序的字段名判定查询时的字段名称
    * @date 2020/7/5 17:17
    * @param orderField  排序的字段名
    * @return java.lang.String
    */
    private  String judgeOrderField(String orderField){
        if(StringUtils.isBlank(orderField)){
            return "id";
        }

        switch (orderField){
            case "startTime": return "start_time";
            case "endTime": return "end_time";
            case "created": return "created";
            default: return "id";
        }
    }

    /**
    * @Description 判断试题状态
    * @date 2020/7/6 17:47
    * @param currentMilli  当前的时间毫秒
    * @param startTimeMilli  开始的时间毫秒
    * @param endTimeMilli   结束的时间毫秒
    * @return java.lang.Integer
    */
    private Integer judgeStatus(Long currentMilli,Long startTimeMilli,Long endTimeMilli){
        if(currentMilli<startTimeMilli){
            return PublicTestStatus.UNOPENED.getValue();
        }else if(currentMilli>=startTimeMilli&&currentMilli<endTimeMilli){
            return PublicTestStatus.PROCESSING.getValue();
        }else{
            return PublicTestStatus.CLOSED.getValue();
        }
    }
}
