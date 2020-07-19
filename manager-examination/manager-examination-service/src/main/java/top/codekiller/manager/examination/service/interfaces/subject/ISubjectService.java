package top.codekiller.manager.examination.service.interfaces.subject;

import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.Subject;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/5/26 23:34
 * @Description 学科服务接口
 */
public interface ISubjectService {


    /**
     * 获取所有学科信息
     * @return
     */
    List<Subject> queryAllSubject();

    /**
     * 获取所有的学科信息
     * @return
     */
    PageResult<Subject> queryAllSubjects(String key, Long page, Long row);


    /**
     * 新增学科信息
     * @param subject
     */
    void insertSubject(Subject subject);


    /**
     * 根据id删除学科信息
     * @param id
     */
    Integer deleteSubjectById(Long id);


    /**
     * 修改学科信息
     * @param subject
     */
    void updateSubject(Subject subject);


    /**
     * 删除的信息重启
     * @param id
     */
    void updateSubjectById(Long id);


    /**
     * 通过id查询学科的名称
     * @param id
     * @return
     */
    String querySubjectNameById(Long id);
}
