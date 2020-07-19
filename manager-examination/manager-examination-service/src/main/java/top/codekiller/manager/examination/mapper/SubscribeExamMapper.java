package top.codekiller.manager.examination.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.mongodb.repository.Query;
import top.codekiller.manager.examination.pojo.ExamResult;
import top.codekiller.manager.examination.pojo.SubscribeExam;
import top.codekiller.manager.examination.pojo.bo.SubscribeExamBo;
import top.codekiller.manager.examination.pojo.data.ExamData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/17 23:27
 * @description 用户订阅试卷操作服务
 */
public interface SubscribeExamMapper extends BaseMapper<SubscribeExam> {


    /**
     * 查询当前用户订阅的信息(用户id和订阅判断)
     * @param userId
     * @return
     */
    @Select("select se.test_id as testId,se.deleted from tb_subscribe_exam se where se.user_id=#{userId} and deleted=0")
    List<SubscribeExam> querySubscribeTestIdAndDeletedByUserId(@Param("userId")Long userId);


    /**
    * @Description 查询订阅考试的相关信息
    * @date 2020/7/5 18:44
    * @param status  订阅记录的状态0:未考试,1:正在考试,2:已考试
    * @param deleted 卷的订阅状态,0:没有订阅,1:已订阅
    * @param userId 用户id
    * @param orderField  排序的字段名
    * @param order  排序的要求
    * @return java.util.List<top.codekiller.manager.examination.pojo.bo.SubscribeExamBo>
    */
    @Select("<script> select se.`id`,se.`user_id` as userId,se.`test_id` as testId,se.`status`,se.`score`,se.`frequency`,se.`created`,se.`begin_work_time` as beginWorkTime,se.`finish_work_time` as finishWorkTime,se.`deleted`," +
            "pt.`created` as publicTestCreated, pt.`start_time` as startTime ,pt.`end_time` as endTime,pt.`status` as publicTestStatus,pt.`id` as publicTestId ,u.`name` as uname,u.`username` as userName " +
            "from tb_public_test pt,tb_subscribe_exam se,tb_user u " +
            "<trim prefix='where'  prefixOverrides='and'> " +
            "<if test='status!=null'> " +
            "and (se.`status`=#{status} ) " +
            "</if> " +
            "and (u.`id`=se.`user_id`) </trim> " +
            "and (se.`deleted`=#{deleted}) " +
            "and (se.`test_id`=pt.`test_id` and pt.`deleted`=0) " +
            "<if test='userId!=null'>" +
            "and (se.`user_id`=#{userId}) " +
            "</if>" +
            "order by ${orderField} ${order} </script>")
    List<SubscribeExamBo> querySubscribeExamBoInfo(@Param("status")Integer status,@Param("deleted")Integer deleted,
                                                   @Param("userId")Long userId,@Param("orderField")String orderField,
                                                   @Param("order")String order);



    /**
    * @Description 根据状态查询id，version，finishWorkTime
    * @date 2020/7/6 22:30
    * @param status
    * @return java.util.List<top.codekiller.manager.examination.pojo.SubscribeExam>
    */
    @Select("select `id`,`version`,`finish_work_time` as finishWorkTime from tb_subscribe_exam where status=#{status} and deleted=0")
    List<SubscribeExam> queryExamInfoByStatus(Integer status);

    /**
     * @Description 根据用户订阅试卷的id查询试卷的结果
     * @date 2020/7/7 18:10
     * @param subscribeExamId
     * @return top.codekiller.manager.examination.pojo.ExamResult
     */
    @Select("select `score`,`id` from tb_subscribe_exam where id=#{subscribeExamId} and `status`=2 and `deleted`=0 ")
    @Results({
            @Result(property = "subscribeExamId", column = "id"),
            @Result(property = "score", column = "score"),
            @Result(property = "examAnswerSituations", column = "id", javaType = List.class,
                    many = @Many(select = "top.codekiller.manager.examination.mapper.ExamAnswerSituationMapper.queryBySubscribeExamId"))
    })
    ExamResult queryAllExamResult(@Param("subscribeExamId") Long subscribeExamId);


    /**
     * 根据用户id查询该用户的所有考试信息(也不不需要用户id查询所有)
     * @param userId
     * @return
     */
    @Select("<script> select `test_id` as testId ,score,`begin_work_time` as beginWorkTime,`finish_work_time` as finishWorkTime,frequency " +
            "from `tb_subscribe_exam` " +
            "<trim prefix='where' prefixOverrides='and'> " +
            "<if test='userId!=null'> " +
            "and `user_id`=#{userId} " +
            "</if>" +
            "and `deleted`=0 and `status`=2 " +
            "</trim> " +
            "order by `finish_work_time` desc </script>")
    List<ExamData> queryScoreInfoByUserId(Long userId);

    /**
     * 根据试卷id查询当前试卷的平均分
     * @param testId
     * @return
     */
    @Select("select coalesce(avg(score),0.00)   from tb_subscribe_exam where test_id=#{testId} and deleted=0 and status=2")
    Double queryAverScoreByTestId(@Param("testId")String testId);


    /**
    * @Description 根据用户的id查询试卷的结束时间
    * @date 2020/7/13 22:53
    * @param userId
    * @return java.util.List<java.time.LocalDateTime>
    */
    @Select("<script> select `finish_work_time` from `tb_subscribe_exam` " +
            "<trim  prefix='where' prefixOverrides='and'> " +
            "<if test='userId!=null'>" +
            "and `user_id`=#{userId} " +
            "</if>" +
            "and `deleted`=0 and `status`=2 " +
            "</trim> </script>")
    List<LocalDateTime> queryFinalWorkTimeByUserId(@Param("userId") Long userId);

    /**
     * @Description 根据用户的订阅id设置开始考试的时间和结束考试时间
     * @date 2020/7/6 11:02
     * @param id  用户的订阅id
     * @param startTime  开始考试的时间
     * @param endTime 结束考试时间
     * @return java.lang.Integer
     */
    @Update("update tb_subscribe_exam set begin_work_time=#{startTime},finish_work_time=#{endTime}," +
            "version=(#{version}+1),status=#{status} where id=#{id} and version=#{version}")
    Integer updateAllWorkTimeById(@Param("id")Long id, @Param("startTime")LocalDateTime startTime,
                                  @Param("endTime")LocalDateTime endTime,@Param("status")Integer status,
                                  @Param("version")Long version);

    /**
     * @Description 根据id修改考试状态
     * @date 2020/7/6 17:21
     * @param id
     * @param status
     * @return java.lang.Integer
     */
    @Update("update tb_subscribe_exam set `status`=#{status},`version`=(#{version}+1) where id=#{id} and version=#{version}")
    Integer updateExamStatusById(@Param("id") Long id, @Param("status") Integer status,@Param("version") Long version);

    /**
    * @Description 更新用户预约试卷的分数
    * @date 2020/7/7 15:51
    * @param totalScore
    * @param id
    * @param version
    * @return java.lang.Integer
    */
    @Update("update tb_subscribe_exam set `score`=#{score},`status`=#{status},`version`=(#{version}+1),`finish_work_time`=#{finishWorkTime},`frequency`=#{frequency} where `id`=#{id} and `version`=#{version}")
    Integer updateScoreAndStatus(@Param("score") double totalScore,@Param("status")Integer status, @Param("id") Long id,
                                 @Param("version") Long version,@Param("finishWorkTime")LocalDateTime localDateTime,
                                 @Param("frequency")Integer frequency);



}
