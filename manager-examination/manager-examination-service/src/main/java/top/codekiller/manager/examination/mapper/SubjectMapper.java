package top.codekiller.manager.examination.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.codekiller.manager.examination.pojo.Subject;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/5/26 23:34
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    /**
     * 获取全部的学科信息
     * @return
     */
    @Select("select * from tb_subject where deleted=0")
    List<Subject> queryAllSubject();

    /**
     * 通过id查询学科的名称
     * @param id
     * @return
     */
    @Select("select name from tb_subject where id=#{id}")
    String querySubjectNameById(Long id);
}
