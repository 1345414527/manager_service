package top.codekiller.manager.examination.service.impl.topic;
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
import top.codekiller.manager.examination.properties.RedisNameProperties;
import top.codekiller.manager.examination.utils.MongoLogUtils;
import top.codekiller.manager.examination.utils.MongodbUtils;
import top.codekiller.manager.examination.repository.TopicRepository;
import top.codekiller.manager.examination.pojo.Topic;
import top.codekiller.manager.examination.pojo.bo.TopicBo;
import top.codekiller.manager.examination.service.interfaces.subject.ISubjectService;
import top.codekiller.manager.examination.service.interfaces.topic.ITopicService;
import top.codekiller.manager.examination.utils.RedisUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author codekiller
 * @date 2020/5/30 20:49
 * @Description 试题服务接口实现类
 */
@Service
@Slf4j
public class TopicServiceImpl implements ITopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisNameProperties redisNameProperties;

    /**
     * 查询所有的试题
     * @return
     */
    @Override
    public List<Topic> queryAllTopics() {
        List<Topic> topics = MongodbUtils.findAll(Topic.class);
        return topics;
    }


    /**
     * 根据条件查询试题
     * @param key     关键字(包含名称， 备注)
     * @param page    页码数
     * @param row     页行数
     * @param type    试题类型0：选择题，1：判断题
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @return
     */
    @Override
    public PageResult<TopicBo> queryAllTopics(String key, Integer page, Integer row, Integer type, String subjectStr, String orderField, Integer order) {
        Criteria criteria=new Criteria();


        //关键字模糊查询
        if(StringUtils.isNotBlank(key)){
            criteria= criteria.orOperator(Criteria.where("name").regex("^.*"+key+".*$"),Criteria.where("note").regex("^.*"+key+".*$"),
                                          Criteria.where("select").regex("^.*"+key+".*$"),Criteria.where("id").is(key) );
        }


        //查询指定类型的试题
        if(type!=null){
            criteria.and("type").is(type);
        }

        //查询指定学科的试题
        if(StringUtils.isNotBlank(subjectStr)){
            Long subject=Long.parseLong(subjectStr);
            criteria.and("subject").is(subject);
        }

        Query query=new Query();
        query.addCriteria(criteria);


        //分页操作
         //先查询总量
        long count = this.mongoTemplate.count(query, Topic.class);

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

        List<Topic> topics = this.mongoTemplate.find(query, Topic.class);


        //将试题集合进行转化为前台展示的试题集合
        List<TopicBo>  topicBos= topics.stream().map(topic -> {
            //获取题目的学科名称
            String subjectName = this.subjectService.querySubjectNameById(topic.getSubject());
            TopicBo topicBo=new TopicBo(topic.getId(),topic.getName(),topic.getSelect(),topic.getAnswer(),topic.getType(),judgeTopicType(topic.getType()),topic.getSubject(),
                                        subjectName, topic.getNote(),topic.getImage(),topic.getCreated(),topic.getDeleted());
            return topicBo;
        }).collect(Collectors.toList());

        //封装进分页结果类中
        PageResult<TopicBo> pageResult=new PageResult<>(count,totalPage,topicBos);
        System.out.println(pageResult);

        return pageResult;
    }


    /**
    * @Description 判断选择题类型
    * @date 2020/7/16 0:07
    * @param type
    * @return java.lang.String
    */
    private String judgeTopicType(Integer type){
        switch (type){
            case 0: return  "选择题";
            case 1: return  "判断题";
            default: return "其他类型";
        }
    }

    /**
     * 通过id集合获取试题集合
     * @param topics
     * @return
     */
    @Override
    public List<Topic> queryTopicsById(List<String> topics) {
       return  topics.stream().map((id)->{
            Topic topic = this.mongoTemplate.findById(id, Topic.class);
            return topic;
        }).collect(Collectors.toList());
    }

    /**
     * 插入新的试题信息
     * @param topic
     */
    @Override
    public void insertTopic(Topic topic) {
        topic.setId(null);
        topic.setCreated(new Date());
        topic.setDeleted(false);

        //新增试题信息
        MongodbUtils.save(topic);

        MongoLogUtils.insertExamLog("新增了"+topic.getSubject()+"学科的试题"+topic.getId());

        //新增缓存
        String key=this.redisNameProperties.getTopicName()+"info:"+topic.getId();
        if(!RedisUtils.hasKey(key)) {
            RedisUtils.set(key, topic);
        }
    }


    /**
     * 更新试题信息
     * @param topic
     */
    @Override
    public void updateTopic(Topic topic) {
        Query query=new Query(Criteria.where("id").is(topic.getId()));
        boolean exists = this.mongoTemplate.exists(query, Topic.class);

        //判断数据是否存在
        if(exists){
            try {
                MongodbUtils.save(topic);
            } catch (Exception e) {
                log.error("修改试题数据错误",e);
            }
        }
    }

    /**
     * 删除(启用)试题
     * @param id
     */
    @Override
    public void deleteTopic(String id) {
        Query query=new Query(Criteria.where("id").is(id));
        Topic topic = this.mongoTemplate.findOne(query, Topic.class);

        //进行修改
        try {
            if(topic!=null){
                Update update=new Update();
                update.set("deleted",!topic.getDeleted());
                this.mongoTemplate.updateFirst(query,update,Topic.class);
            }
        } catch (Exception e) {
            log.error("试题删除(启用)失败",e);
        }
    }
}
