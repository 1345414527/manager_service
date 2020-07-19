package top.codekiller.manager.examination.service.interfaces.topic;

import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.ExamAnswerSituation;
import top.codekiller.manager.examination.pojo.topicResult.TopicAwResult;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/15 23:12
 * @Description 试题的答题情况数据服务接口
 */
public interface ITopicAnswerResultService {

    /**
    * @Description 根据条件查询试题的答题情况
    * @date 2020/7/15 23:51
    * @param key
    * @param page
    * @param row
    * @param type
    * @param subjectStr
    * @param orderField
    * @param order
    * @return top.codekiller.manager.common.pojo.PageResult<top.codekiller.manager.examination.pojo.topicResult.TopicAwResult>
    */
    PageResult<TopicAwResult> calculateTopicAnswerResult(String key, Integer page, Integer row, Integer type, String subjectStr, String orderField, Integer order);

    /**
    * @Description 根据试题id查询所有的答题情况
    * @date 2020/7/16 0:20
    * @param topicId
    * @return java.util.List<top.codekiller.manager.examination.pojo.ExamAnswerSituation>
    */
    List<ExamAnswerSituation> queryTopicAnswerSituationByTopicId(String topicId);
}
