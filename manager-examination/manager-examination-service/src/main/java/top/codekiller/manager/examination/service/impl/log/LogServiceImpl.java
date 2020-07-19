package top.codekiller.manager.examination.service.impl.log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.Test;
import top.codekiller.manager.examination.pojo.log.ExamLog;
import top.codekiller.manager.examination.repository.ExamLogRepository;
import top.codekiller.manager.examination.service.interfaces.log.ILogService;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/16 15:39
 * @Description 日志查询服务接口的实现
 */

@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * @Description 条件分页查询所有考试日志
     * @date 2020/7/16 15:57
     * @param key  用户名，用户id，信息
     * @param page  当前页码
     * @param row  当前页码的数据行数
     * @param orderField  排序字段
     * @param order  排序的顺序,默认为升序
     * @return top.codekiller.manager.common.pojo.PageResult<top.codekiller.manager.examination.pojo.log.ExamLog>
     */
    @Override
    public PageResult<ExamLog> queryAllExamLog(String key, Integer page, Integer row, String orderField, Integer order) {
        Criteria criteria=new Criteria();

        System.out.println("key值"+key);
        //关键字模糊查询
        if(StringUtils.isNotBlank(key)){
            criteria= criteria.orOperator(Criteria.where("userName").regex("^.*"+key+".*$"),Criteria.where("info").regex("^.*"+key+".*$"),
                                          Criteria.where("userId").is(key),Criteria.where("id").is(key),Criteria.where("create").regex("^.*"+key+".*$"));
        }

        Query query=new Query();
        query.addCriteria(criteria);


        //分页操作
        //先查询总量
        long count = this.mongoTemplate.count(query, ExamLog.class);

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

        List<ExamLog> examLogs = this.mongoTemplate.find(query, ExamLog.class);


        //封装进分页结果类中
        PageResult<ExamLog> pageResult=new PageResult<>(count,totalPage,examLogs);


        return pageResult;
    }
}
