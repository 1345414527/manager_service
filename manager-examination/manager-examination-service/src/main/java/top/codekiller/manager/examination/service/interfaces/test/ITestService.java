package top.codekiller.manager.examination.service.interfaces.test;

import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.Test;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/1 16:26
 * @Description 试卷服务接口
 */
public interface ITestService {

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
    PageResult<Test> queryAllTest(String key, Integer page, Integer row, String subject, Boolean publish, String orderField, Integer order);

    /**
     * 根据关键字和学科类型条件查询所有已发布试卷
     * @return
     */
    List<Test> queryAllTest(String key,String subject);



    /**
     * 通过id查询试卷
     * @param id
     * @return
     */
    Test queryTestById(String id);

    /**
     * 新增试题
     * @param test
     */
    Test insertTest(Test test);

    /**
     * 删除(重启试卷)
     */
    void deleteTest(String id);


    /**
     * 发布(收回)试卷
     * @param id
     */
    void updateTestPublish(String id);


    /**
     * 修改试卷信息
     * @param topic
     */
    void updateTest(Test topic);
}
