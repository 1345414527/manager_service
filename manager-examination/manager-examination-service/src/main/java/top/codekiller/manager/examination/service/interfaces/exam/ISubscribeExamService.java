package top.codekiller.manager.examination.service.interfaces.exam;

import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.SubscribeExam;
import top.codekiller.manager.examination.pojo.bo.SubscribeExamBo;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/17 23:24
 * @description 用户订阅试卷操作服务接口
 */
public interface ISubscribeExamService {

    /**
     * 插入用户订阅试卷记录
     * @param subscribeExam
     * @return
     */
    Integer insertSubscribeExamInfo(SubscribeExam subscribeExam);

    /**
     * 根据用户id查询试卷订阅信息
     * @param userId
     * @return
     */
    List<SubscribeExam> querySubscribeTestIdAndDeletedByUserId(Long userId);

    /**
     * 根据条件查询发布的试卷
     * @param key     关键字(包含名称，备注，学校，出题人)
     * @param page    页码数
     * @param row     页行数
     * @param status 订阅记录的状态0:未考试,1:正在考试,2:已考试
     * @param deleted 试卷的订阅状态,0:未删除,1:已删除
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @param userId 用户的id
     * @return
     */
    PageResult<SubscribeExamBo> queryAllExamInfo(String key, Integer page, Integer row, Integer deleted,String subject, Integer status, String orderField, Integer order,Long userId);


    /**
     * 删除订阅信息(如果当前的考试正在进行或者已经结束了，则不能删除了)
     * @param userId
     * @param publicTestId
     * @return
     */
     Integer deleteSubscribe(Long userId,Long publicTestId);

     /**
     * @Description 删除订阅信息(如果当前的考试正在进行或者已经结束了，也能删除)
     * @date 2020/7/13 10:23
     * @param publicTestId
     * @return java.lang.Integer
     */
     Integer deleteSubscribe(Long publicTestId);
}
