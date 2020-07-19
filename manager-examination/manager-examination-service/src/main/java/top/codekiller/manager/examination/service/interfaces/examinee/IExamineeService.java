package top.codekiller.manager.examination.service.interfaces.examinee;

/**
 * @author codekiller
 * @date 2020/7/8 10:43
 * @Description 考生操作服务接口
 */
public interface IExamineeService {


    /**
    * @Description 重置考生的试卷(连着分数也一起重置)
    * @date 2020/7/12 13:27
    * @param examId
    * @return java.lang.Integer
    */
    Integer reset(long examId);

    /**
    * @Description 重置考生的试卷(考生的分数和每个试题的分数不会改变，再一次做的时候是覆盖分数)
    * @date 2020/7/12 14:09
    * @param examId
    * @return java.lang.Integer
    */
    Integer again(long examId);
}
