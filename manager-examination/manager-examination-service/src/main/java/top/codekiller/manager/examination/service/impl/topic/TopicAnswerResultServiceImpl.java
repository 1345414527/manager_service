package top.codekiller.manager.examination.service.impl.topic;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.mapper.ExamAnswerSituationMapper;
import top.codekiller.manager.examination.pojo.ExamAnswerSituation;
import top.codekiller.manager.examination.pojo.Topic;
import top.codekiller.manager.examination.pojo.bo.TopicBo;
import top.codekiller.manager.examination.pojo.topicResult.TopicAnswerSituation;
import top.codekiller.manager.examination.pojo.topicResult.TopicAwResult;
import top.codekiller.manager.examination.service.interfaces.subject.ISubjectService;
import top.codekiller.manager.examination.service.interfaces.topic.ITopicAnswerResultService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author codekiller
 * @date 2020/7/15 23:12
 * @Description 试题的答题情况数据服务接口的实现类
 */
@Service
public class TopicAnswerResultServiceImpl implements ITopicAnswerResultService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private ExamAnswerSituationMapper examAnswerSituationMapper;

    /**
    * @Description 根据条件查询试题的答题情况
    * @date 2020/7/15 23:52
    * @param key
    * @param page
    * @param row
    * @param type
    * @param subjectStr
    * @param orderField
    * @param order
    * @return top.codekiller.manager.common.pojo.PageResult<top.codekiller.manager.examination.pojo.topicResult.TopicAwResult>
    */
    @Override
    public PageResult<TopicAwResult> calculateTopicAnswerResult(String key, Integer page, Integer row, Integer type, String subjectStr, String orderField, Integer order) {
        Criteria criteria=new Criteria();

        System.out.println("key值"+key);
        //关键字模糊查询
        if(StringUtils.isNotBlank(key)){
            criteria= criteria.orOperator(Criteria.where("name").regex("^.*"+key+".*$"),Criteria.where("note").regex("^.*"+key+".*$"),
                                          Criteria.where("select").regex("^.*"+key+".*$"),Criteria.where("id").is(key),Criteria.where("id").regex("^.*"+key+".*$"));
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
        List<TopicAwResult>  topicAwResults= topics.stream().map(topic -> {
            //判断题目的类型
            String typeStr=this.judgeTopicType(topic.getType());
            //根据试卷id查询考生的答题情况
            List<ExamAnswerSituation> examAnswerSituations = this.queryTopicAnswerSituationByTopicId(topic.getId());

            TopicAwResult topicAwResult=new TopicAwResult();
            topicAwResult.init(topic);
            topicAwResult.setType(this.judgeTopicType(topic.getType()));
            //计算相关数据
            TopicDataUtils.calculateTopicAnswerData(topicAwResult,examAnswerSituations);
            //学科名称
            topicAwResult.setSubject(this.subjectService.querySubjectNameById(topic.getSubject()));

            return topicAwResult;
        }).collect(Collectors.toList());

        //封装进分页结果类中
        PageResult<TopicAwResult> pageResult=new PageResult<>(count,totalPage,topicAwResults);
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
   * @Description 根据试题id查询所有的答题情况
   * @date 2020/7/16 0:20
   * @param topicId
   * @return java.util.List<top.codekiller.manager.examination.pojo.ExamAnswerSituation>
   */
    @Override
    public List<ExamAnswerSituation> queryTopicAnswerSituationByTopicId(String topicId) {
        return this.examAnswerSituationMapper.queryTopicAnswerSituationByTopicId(topicId);
    }


    /**
    * @author codekiller
    * @date 2020/7/16 0:30
    * @Description 试题计算操作内部类
    */
    private static class TopicDataUtils{

        /**
         * @Description 计算相关数据模板(回答的总数,错误比例,最多的回答情况(选项，人数))
         *              可作为对外的模板方法
         * @date 2020/7/16 0:27
         * @param topicAwResult
         * @param examAnswerSituations
         * @return void
         */
        public static  void calculateTopicAnswerData(TopicAwResult topicAwResult,List<ExamAnswerSituation> examAnswerSituations){
            topicAwResult.setAnswerNum(TopicDataUtils.calculateTopicAnswerCount(examAnswerSituations));
            TopicDataUtils.calculateTopicAnswerSituation(examAnswerSituations,topicAwResult);
        }

        /**
        * @Description 计算试题的总答题数量
        * @date 2020/7/16 0:33
        * @param examAnswerSituations
        * @return java.lang.Integer
        */
        public static Integer calculateTopicAnswerCount(List<ExamAnswerSituation> examAnswerSituations) {
            return examAnswerSituations.size();
        }


        /**
        * @Description 计算试题的错误比例,最多的回答情况(选项，人数)
        * @date 2020/7/16 0:38
        * @param examAnswerSituations
        * @return java.lang.Double
        */
        public static void calculateTopicAnswerSituation(List<ExamAnswerSituation> examAnswerSituations,TopicAwResult topicAwResult){
            //当该试题没有人做到的时候
            if(CollectionUtils.isEmpty(examAnswerSituations)){
                topicAwResult.setErrorRate(0.0);
                topicAwResult.setTopicAnswerSituation(new TopicAnswerSituation("-2",0L));
                return;
            }

            //答错的数量
            int errorCount=0;
            List<TopicAnswerSituation> topicAnswerSituations=new ArrayList<>();
            for (ExamAnswerSituation examAnswerSituation : examAnswerSituations) {
                if(examAnswerSituation.getAnswerSituation()==0){
                    ++errorCount;
                }
                TopicDataUtils.calculateTopicAnswerCountSituation(topicAnswerSituations,examAnswerSituation.getUserAnswer());
            }
            //从大到小排序
            Collections.sort(topicAnswerSituations);

            topicAwResult.setErrorRate(errorCount*1.0/examAnswerSituations.size());
            topicAwResult.setTopicAnswerSituation(topicAnswerSituations.get(0));

        }


        /**
        * @Description 判断试题回答的答案情况，增加该种回答的数量
        * @date 2020/7/16 0:53
        * @param topicAnswerSituations
        * @param answer
        * @return void
        */
        private static void calculateTopicAnswerCountSituation(List<TopicAnswerSituation> topicAnswerSituations,String answer){
            for (TopicAnswerSituation topicAnswerSituation : topicAnswerSituations) {
                if(StringUtils.equalsIgnoreCase(topicAnswerSituation.getAnswer(),answer)){
                    topicAnswerSituation.countIncrese();
                    return;
                }
            }
            topicAnswerSituations.add(new TopicAnswerSituation(answer,1L));

        }
    }



}
