package top.codekiller.manager.examination.service.impl.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.Test;
import top.codekiller.manager.examination.service.interfaces.test.IPublicTestService;
import top.codekiller.manager.examination.service.interfaces.test.ITestService;
import top.codekiller.manager.examination.utils.MongodbUtils;
import java.util.Date;
import java.util.List;


/**
 * @author codekiller
 * @date 2020/6/1 16:26
 * @Description 试卷服务接口的实现类
 */

@Service
@Slf4j
public class TestServiceImpl implements ITestService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IPublicTestService publicTestService;




    /**
     * 根据条件查询试卷
     * @param key     关键字(包含名称，备注，学校，出题人)
     * @param page    页码数
     * @param row     页行数
     * @param publish 试卷发布情况
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @return
     */
    @Override
    public PageResult<Test> queryAllTest(String key, Integer page, Integer row, String subject, Boolean publish, String orderField, Integer order) {
        Criteria criteria=new Criteria();


        //关键字模糊查询
        if(StringUtils.isNotBlank(key)){
            criteria= criteria.orOperator(Criteria.where("name").regex("^.*"+key+".*$"),Criteria.where("note").regex("^.*"+key+".*$"),
                                          Criteria.where("school").regex("^.*"+key+".*$"),Criteria.where("creator").regex("^.*"+key+".*$"),
                                          Criteria.where("id").is(key) );
        }
        //查询指定学科的试卷
        if(StringUtils.isNotBlank(subject)){
            Long subjectl=Long.parseLong(subject);
            criteria.and("subject").is(subjectl);
        }

        //查询指定发布情况的试卷
        if(publish!=null){
            criteria.and("publish").is(publish);
        }

        Query query=new Query();
        query.addCriteria(criteria);


        //分页操作
        //先查询总量
        long count = this.mongoTemplate.count(query, Test.class);

        //总页数
        int totalPage=(int)(count%row==0?count/row:count/row+1);

        //偏移量
        int offset=(page-1)*row;

        //添加分页条件
        query.skip(offset).limit(row);

        //进行排序
        if(StringUtils.isNotBlank(orderField)) {
            if(order==0) {
                query.with(Sort.by(Sort.Order.asc(orderField)));
            }else if(order==1){
                query.with(Sort.by(Sort.Order.desc(orderField)));
            }
        }

        List<Test> tests = this.mongoTemplate.find(query, Test.class);


        //封装进分页结果类中
        PageResult<Test> pageResult=new PageResult<>(count,totalPage,tests);

        return pageResult;
    }


    /**
     * 根据关键字和学科条件查询所有已发布试卷
     * @return
     */
    @Override
    public List<Test> queryAllTest(String key,String subject) {
        Query query=new Query();
        Criteria criteria = Criteria.where("publish").is(true);
        //关键字模糊查询
        if(StringUtils.isNotBlank(key)) {
            System.out.println("key"+key);
            criteria = criteria.orOperator(Criteria.where("name").regex("^.*" + key + ".*$"), Criteria.where("note").regex("^.*" + key + ".*$"),
                    Criteria.where("school").regex("^.*" + key + ".*$"), Criteria.where("creator").regex("^.*" + key + ".*$"),Criteria.where("id").is(key));
        }

        //查询指定学科的试卷
        if(StringUtils.isNotBlank(subject)){
            System.out.println("subject"+subject);
            Long subjectl=Long.parseLong(subject);
            criteria.and("subject").is(subjectl);
        }

        query.addCriteria(criteria);
        List<Test> tests = this.mongoTemplate.find(query, Test.class);
        return tests;
    }


    /**
     * 通过id查询试卷
     * @param id
     * @return
     */
    @Override
    public Test queryTestById(String id) {
       return  this.mongoTemplate.findOne(new Query(Criteria.where("id").is(id)),Test.class);
    }

    /**
     * 新增试卷
     * @param test
     */
    @Override
    public Test insertTest(Test test) {
        test.setId(null);
        test.setCreated(new Date());
        test.setDeleted(false);
        test.setPublish(false);

        try {
            MongodbUtils.save(test);
            System.out.println(test);
            return test;
        } catch (Exception e) {
            log.error("试卷新增失败",e);
        }
        return null;
    }


    /**
     * 删除重启试卷
     */
    @Override
    public void deleteTest(String id) {
        Query query=new Query(Criteria.where("id").is(id));
        Test test = this.mongoTemplate.findOne(query, Test.class);
        try {
            if(test!=null){
                Update update=new Update();
                update.set("deleted",!test.getDeleted());

                //如果是删除操作，取消发布，并且删除考试信息
                if(!test.getDeleted()) {
                    update.set("publish",false);
                   this.publicTestService.deletePublicTest(test.getId());
                }
                this.mongoTemplate.updateFirst(query,update,Test.class);
            }
        } catch (Exception e) {
            log.error("删除(重启)失败",e);
        }
    }

    /**
     * 发布(收回)试卷
     * @param id
     */
    @Override
    public void updateTestPublish(String id) {
        Query query=new Query(Criteria.where("id").is(id));
        Test test = this.mongoTemplate.findOne(query, Test.class);

        try {
            if(test!=null){
                Update update=new Update();
                update.set("publish",!test.getPublish());
                this.mongoTemplate.updateFirst(query,update,Test.class);

                //判断是否是发布
                if(!test.getPublish()) {
                    //插入发布试卷记录
                    this.publicTestService.insertPublicTest(test.getId());
                }else{
                    //删除发布试卷记录
                   this.publicTestService.deletePublicTest(test.getId());
                }
            }
        } catch (Exception e) {
            log.error("试卷发布(收回)失败",e);
        }
    }

    /**
     * 修改试卷信息
     * @param test
     */
    @Override
    public void updateTest(Test test) {
        Query query=new Query(Criteria.where("id").is(test.getId()));
        boolean exists = this.mongoTemplate.exists(query, Test.class);

        //判断数据是否存在
        if(exists){
            try {
                MongodbUtils.save(test);
            } catch (Exception e) {
                log.error("修改试卷数据错误",e);
            }
        }
    }
}
