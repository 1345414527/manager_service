package top.codekiller.manager.examination.service.interfaces.log;

import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.examination.pojo.log.ExamLog;

/**
 * @author codekiller
 * @date 2020/7/16 15:39
 * @Description 日志查询服务接口
 */
public interface ILogService {


    /**
     * @Description 条件分页查询所有考试日志
     * @date 2020/7/16 15:57
     * @param key  用户名，用户id，信息
     * @param page  当前页码
     * @param row  当前页码的数据行数
     * @param orderField  排序字段
     * @param order  排序的顺序,默认为升序
     * @return top.codekiller.manager.common.pojo.PageResult<top.codekiller.manager.examination.pojo.log.ExamLog>
     */
    PageResult<ExamLog> queryAllExamLog(String key, Integer page, Integer row, String orderField, Integer order);
}
