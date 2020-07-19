package top.codekiller.manager.examination.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.codekiller.manager.examination.pojo.ExamAnswerSituation;
import top.codekiller.manager.examination.pojo.Topic;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/7 16:06
 * @Description 试卷答题的情况
 */
public interface ExamAnswerSituationMapper extends BaseMapper<ExamAnswerSituation> {


    @Select("select `topic_id` as topicId,`user_answer` as userAnswer,`answer_situation` as answerSituation,`score`  " +
            "from `tb_exam_answer_situation` " +
            "where `subscribe_exam_id`=#{subscribeExamId} and deleted=0")
    ExamAnswerSituation queryBySubscribeExamId(@Param("subscribeExamId")Long subscribeExamId);

    @Select("select `id` from `tb_exam_answer_situation` where `deleted`=0 and `subscribe_exam_id`=#{subscribeExamId}")
    List<Long> queryIdBySubscribeExamId(@Param("subscribeExamId")Long subscribeExamId);


    /**
    * @Description 根据试题id查询所有的答题情况
    * @date 2020/7/16 0:19
    * @param topicId
    * @return java.util.List<top.codekiller.manager.examination.pojo.Topic>
    */
    @Select("select `user_answer` as userAnswer ,`answer_situation` as answerSituation from `tb_exam_answer_situation`  " +
            "where `topic_id`=#{topicId} and `deleted`=0")
    List<ExamAnswerSituation> queryTopicAnswerSituationByTopicId(@Param("topicId") String topicId);
}
