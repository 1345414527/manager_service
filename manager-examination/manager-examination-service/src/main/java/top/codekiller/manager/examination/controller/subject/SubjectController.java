package top.codekiller.manager.examination.controller.subject;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.pojo.Subject;
import top.codekiller.manager.examination.service.interfaces.subject.ISubjectService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/26 23:33
 */

@RestController
@Api(value="学科操作服务接口",tags = "学科操作服务接口")
public class SubjectController {

    @Autowired
    private ISubjectService subjectService;




    /**
     * 获取所有学科信息
     * @return
     */
    @GetMapping("/allSubjects")
    @ApiOperation(value="获取全部学科信息",notes="无条件获取",httpMethod = "GET")
    public ResponseEntity<Map<String,Object>> queryAllSubject(){
        List<Subject> list=this.subjectService.queryAllSubject();
        if(list==null){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.INTERNAL_SERVER_ERROR.value(),"服务器内部错误"));
        }
        Map<String, Object> info = ControllerUtils.getPublicBackValue(HttpStatus.OK.value(), "获取学科信息成功");
        info.put("subjects",list);
        return ResponseEntity.ok(info);

    }


    /**
     * 根据条件获取学科信息
     * @param key 关键字
     * @param page 当前页数
     * @param row  当前页条数
     * @return
     */
    @GetMapping("/subjects")
    @ApiOperation(value="获取所有的学科信息",notes = "有条件获取",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",dataType = "String",paramType = "query",value="关键字信息，包含名称和备注",required = false),
            @ApiImplicitParam(name="page",dataType = "Long",paramType = "query",value="当前页码",required = false,defaultValue = "1"),
            @ApiImplicitParam(name="row",dataType = "Long",paramType = "query",value="当前页码的数据行数",required = false,defaultValue = "5")
    })
    public ResponseEntity<Map<String,Object>> queryAllSubjects(@RequestParam(value="key",required = false)String key,
                                                               @RequestParam(value="page",defaultValue = "1")Long page,
                                                               @RequestParam(value="row",defaultValue = "5")Long row){
        PageResult<Subject> result =subjectService.queryAllSubjects(key,page,row);
        if(result==null){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.INTERNAL_SERVER_ERROR.value(),"服务器内部错误"));
        }
        Map<String, Object> info = ControllerUtils.getPublicBackValue(HttpStatus.OK.value(), "获取学科信息成功");
        info.put("subjects",result);
        return ResponseEntity.ok(info);
    }



    /**
     * 插入学科信息
     * @param subject
     * @return
     */
    @PostMapping("/subjects")
    @ApiOperation(value="插入新的学科信息",notes = "插入新的学科信息",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>> insertSubject(@RequestBody @Valid Subject subject){
        this.subjectService.insertSubject(subject);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(),"新增学科信息成功"));
    }


    /**
     * 根据id删除学科信息
     * @param id
     * @return
     */
    @DeleteMapping("/subjects/{id}")
    @ApiOperation(value="删除学科",notes = "逻辑删除",httpMethod = "DELETE")
    @ApiImplicitParam(name="id",dataType = "Long",paramType = "path",value="学科的id",required = true)
    public ResponseEntity<Map<String,Object>> deleteSubjectById(@PathVariable("id")Long id) {
            this.subjectService.deleteSubjectById(id);
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.OK.value(), id + "删除成功"));
    }


    /**
     * 删除的信息重启
     * @param id
     * @return
     */
    @PutMapping("/subjects/{id}")
    @ApiOperation(value="删除的信息重启",notes = "将已删除的信息变为不删除",httpMethod = "PUT")
    @ApiImplicitParam(name="id",dataType = "Long",paramType = "path",value="学科的id",required = true)
    public ResponseEntity<Map<String,Object>> updateSubjectById(@PathVariable("id")Long id){
        this.subjectService.updateSubjectById(id);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.OK.value(), id + "重启成功"));
    }


    /**
     * 修改学科信息
     * @param subject
     * @return
     */
    @PutMapping("/subjects")
    @ApiOperation(value="修改学科信息",notes="修改学科的信息",httpMethod = "PUT")
    public ResponseEntity<Map<String,Object>> updateSubjectById(@RequestBody Subject subject){
        this.subjectService.updateSubject(subject);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.ACCEPTED.value(),subject.getId()+"修改成功"));
    }
}
