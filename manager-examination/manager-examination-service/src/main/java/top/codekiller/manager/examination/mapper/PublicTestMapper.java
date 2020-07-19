package top.codekiller.manager.examination.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.codekiller.manager.examination.pojo.PublicTest;
import top.codekiller.manager.examination.pojo.SubscribeExam;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/10 23:25
 * @description DES
 */
public interface PublicTestMapper  extends BaseMapper<PublicTest> {


    /**
     * 更新试卷的开始和结束时间
     * @param publicTest
     * @return
     */
    @Update("update tb_public_Test set start_time=#{publicTest.startTime},status=#{publicTest.status},end_time=#{publicTest.endTime},version=#{publicTest.version} where id=#{publicTest.id}")
    Boolean updatePublicTestTime(@Param("publicTest") PublicTest publicTest);

    /**
     * 查询发布试卷的版本
     * @param id
     * @return
     */
    @Select("select version from tb_public_test where id=#{id}")
    Long queryVersionById(@Param("id")Long id);

    /**
     * 查询试卷的id，开始时间，结束时间,状态，版本
     * @return
     */
    @Select("select id,start_time as startTime,end_time as endTime,status,version from tb_public_test where deleted=0")
    List<PublicTest> queryAllPublicTests();


    /**
     * 通过id修改试卷的状态0:未开始；1：开启中；2：已结束
     * @param id
     * @param status
     * @return
     */
    @Update("update tb_public_test set status=#{status},version=#{version} where id=#{id}")
    Boolean updatePublicTestStatusById(@Param("id") Long id,@Param("status") Integer status,@Param("version")Long version);


    /**
     * 逻辑删除试卷(修改状态和deleted)
     * @param testId
     * @return
     */
    @Update("update tb_public_test set status=-2,deleted=1 where test_id=#{testId} and deleted=0 and status!=-2")
    Boolean deletePublicTestByTestId(@Param("testId")String testId);

    /**
     * 根据id查询试卷的id,开始时间和结束时间
     * @param id
     * @return
     */
    @Select("select test_id as testId, start_time as startTime,end_time as endTime from tb_public_test where id=#{id} ")
    PublicTest queryStartTimeAndEndTimeById(@Param("id")Long id);
}
