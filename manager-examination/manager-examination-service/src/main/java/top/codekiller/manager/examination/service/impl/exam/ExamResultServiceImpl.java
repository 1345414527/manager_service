package top.codekiller.manager.examination.service.impl.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.codekiller.manager.examination.mapper.SubscribeExamMapper;
import top.codekiller.manager.examination.pojo.ExamResult;
import top.codekiller.manager.examination.service.interfaces.exam.IExamResultService;

/**
 * @author codekiller
 * @date 2020/7/8 9:27
 * @Description 考试结果的接口实现
 */
@Service
public class ExamResultServiceImpl implements IExamResultService {

    @Autowired
    private SubscribeExamMapper subscribeExamMapper;

    /**
     * @Description 根据用户订阅试卷的id查询试卷的结果
     * @date 2020/7/7 18:10
     * @param subscribeExamId
     * @return java.util.List<top.codekiller.manager.examination.pojo.ExamResult>
     */
    @Override
    public ExamResult queryAllExamResult(Long subscribeExamId) {
        ExamResult results=this.subscribeExamMapper.queryAllExamResult(subscribeExamId);
        return results;
    }
}
