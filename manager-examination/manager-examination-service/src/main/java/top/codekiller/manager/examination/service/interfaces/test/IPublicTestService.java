package top.codekiller.manager.examination.service.interfaces.test;

import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.PublicTest;
import top.codekiller.manager.examination.pojo.bo.PublicTestBo;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/11 0:42
 * @description 公布试卷服务接口
 */
public interface IPublicTestService {

    /**
     * 根据testId查询试卷发布记录
     * @param testId
     * @return
     */
    PublicTest queryPublicTestByTestId(String testId);


    /**
     * 根据testId删除试卷发布记录
     * @param testId
     */
    void deletePublicTest(String testId);

    /**
     * 插入testId对应的试卷发布记录
     * @param testId
     */
    void insertPublicTest(String testId);


    /**
     * 根据条件查询发布的试卷
     * @param key     关键字(包含名称，备注，学校，出题人)
     * @param page    页码数
     * @param row     页行数
     * @param status 试卷的状态,-1:初始化,0:未开始,1:开启中,2:已结束
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @return
     */
    PageResult<PublicTestBo> queryAllPublicTest(String key, Integer page, Integer row, String subject, Integer status, String orderField, Integer order);


    /**
     * 修改试卷的发布和结束时间
     * @param publicTest
     * @return
     */
    Boolean updatePublicTestTime(PublicTest publicTest);


    /**
     * 更新发布试卷的状态
     * @param publicTest
     * @param status
     * @return
     */
    Boolean updatePublicTestStatus(PublicTest publicTest,Integer status);

    /**
     * 查询已发布试卷的id，开始时间，结束时间,状态，版本
     * @return
     */
    List<PublicTest> queryAllPublicTests();

    /**
     * 根据id查询试卷的开始时间和结束时间
     * @param id
     * @return
     */
    PublicTest queryStartTimeAndEndTimeById(Long id);
}
