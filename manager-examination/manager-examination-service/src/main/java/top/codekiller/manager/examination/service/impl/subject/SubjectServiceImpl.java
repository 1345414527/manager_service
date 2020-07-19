package top.codekiller.manager.examination.service.impl.subject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.common.utils.PinYinUtil;
import top.codekiller.manager.examination.utils.MongoLogUtils;
import top.codekiller.manager.examination.utils.RedisUtils;
import top.codekiller.manager.examination.mapper.SubjectMapper;
import top.codekiller.manager.examination.pojo.Subject;
import top.codekiller.manager.examination.properties.RedisNameProperties;
import top.codekiller.manager.examination.service.interfaces.subject.ISubjectService;
import java.util.Date;
import java.util.List;


/**
 * @author codekiller
 * @date 2020/5/26 23:34
 * @Description 学科服务接口的实现类
 */

@Service
public class SubjectServiceImpl implements ISubjectService {

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private RedisNameProperties redisNameProperties;

    /**
     * 获取所有学科信息
     * @return
     */
    @Override
    public List<Subject> queryAllSubject() {
        List<Subject> subjects = this.subjectMapper.queryAllSubject();
        return subjects;
    }

    /**
     * 获取所有学科信息
     * @return
     */
    @Override
    public PageResult<Subject> queryAllSubjects(String key, Long page, Long row) {
        QueryWrapper<Subject> query = new QueryWrapper<>();
        if(StringUtils.isNotBlank(key)){
            query.like("name",key).or().like("note",key).or().like("id",key);
        }
        Page<Subject> pageInfo = subjectMapper.selectPage(new Page<Subject>(page, row), query);
        PageResult<Subject> result=new PageResult<>(pageInfo.getTotal(),(int)pageInfo.getPages(),pageInfo.getRecords());
        return result;
    }


    /**
     * 新增学科信息
     * @param subject
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSubject(Subject subject) {
        subject.setId(null);
        subject.setCreated(new Date());
        subject.setIcon("iconfont icon-xuexiao_kemu");
        String name2en= PinYinUtil.getPingYin(subject.getName());
        subject.setIndex("/home/subject/"+name2en);
        this.subjectMapper.insert(subject);

        //日志记录
        MongoLogUtils.insertExamLog("新增了一个学科"+subject.getName());


        //新增缓存
        String key=this.redisNameProperties.getSubjectName()+"name:"+subject.getId();
        if(!RedisUtils.hasKey(key)){
            RedisUtils.set(key,subject.getName());
        }
    }


    /**
     * 根据id删除学科信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteSubjectById(Long id) {
        Subject subject = this.subjectMapper.selectById(id);
        subject.setDeleted(true);
        this.subjectMapper.updateById(subject);

        return 0;
    }


    /**
     * 修改学科信息
     * @param subject
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSubject(Subject subject) {
        this.subjectMapper.updateById(subject);

        //日志记录
        MongoLogUtils.insertExamLog("修改了一个学科的信息"+subject.getName()+": "+subject.getNote());

        //更新缓存
        String key=this.redisNameProperties.getSubjectName()+"name:"+subject.getId();
        if(RedisUtils.hasKey(key)){
            RedisUtils.set(key,subject.getName());
        }
    }


    /**
     * 删除的学科重启
     * @param id
     */
    @Override
    public void updateSubjectById(Long id) {
        Subject subject = this.subjectMapper.selectById(id);
        subject.setDeleted(false);
        this.subjectMapper.updateById(subject);
        System.out.println("重启");
    }

    /**
     * 通过id查询学科的名称
     * @param id
     * @return
     */
    @Override
    public String querySubjectNameById(Long id) {
        String key=this.redisNameProperties.getSubjectName()+"name:"+id;
        if(RedisUtils.hasKey(key)){

            return (String) RedisUtils.get(key);
        }

        String name = this.subjectMapper.querySubjectNameById(id);

        if(StringUtils.isNotBlank(name)) {
            RedisUtils.set(key, name);
        }

        return name;
    }
}
