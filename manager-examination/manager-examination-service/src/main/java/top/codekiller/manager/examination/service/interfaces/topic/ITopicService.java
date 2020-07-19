package top.codekiller.manager.examination.service.interfaces.topic;

import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.Topic;
import top.codekiller.manager.examination.pojo.bo.TopicBo;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/5/30 20:48
 * @Description 试题服务接口
 */
public interface ITopicService {

    /**
     * 查询所有试题
     * @return
     */
    List<Topic> queryAllTopics();


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
    PageResult<TopicBo> queryAllTopics(String key, Integer page, Integer row, Integer type, String subjectStr, String orderField, Integer order);


    /**
     * 通过id集合获取试题集合
     * @param topics
     * @return
     */
    List<Topic> queryTopicsById(List<String> topics);

    /**
     * 插入新的试题
     * @param topic
     */
    void insertTopic(Topic topic);


    /**
     * 修改试题信息
     * @param topic
     */
    void updateTopic(Topic topic);

    /**
     * 删除(启用)试题
     * @param id
     */
    void deleteTopic(String id);



}
