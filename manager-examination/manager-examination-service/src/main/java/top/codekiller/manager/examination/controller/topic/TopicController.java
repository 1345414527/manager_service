package top.codekiller.manager.examination.controller.topic;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.pojo.Topic;
import top.codekiller.manager.examination.pojo.bo.TopicBo;
import top.codekiller.manager.examination.service.interfaces.topic.ITopicService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/30 20:36
 */

@RestController
@Api(value="试题操作服务接口",tags = "试题操作服务接口")
public class TopicController {

    @Autowired
    private ITopicService topicService;


    /**
     * 查询所有试题
     * @return
     */
    @GetMapping("/allTopics")
    @ApiOperation(value="查询所有试题",notes="无条件查询",httpMethod = "GET")
    public ResponseEntity<Map<String,Object>> queryAllTopics(){
        List<Topic> topics=this.topicService.queryAllTopics();
        Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.TOPIC_OK.getValue(), "试题查询成功");
        result.put("topics",topics);
        return ResponseEntity.ok(result);
    }


    /**
     * 根据条件查询试题
     * @param key     关键字(包含名称， 备注)
     * @param page    页码数
     * @param row     页行数
     * @param type    试题类型0：选择题，1：判断题
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @return
     */
    @GetMapping("/topics")
    @ApiOperation(value="条件分页查询所有试题",notes = "条件查询",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",dataType = "String",paramType = "query",value="关键字信息，包含名称， 备注",required = false),
            @ApiImplicitParam(name="page",dataType = "Long",paramType = "query",value="当前页码",defaultValue = "1"),
            @ApiImplicitParam(name="row",dataType = "Long",paramType = "query",value="当前页码的数据行数",defaultValue = "5"),
            @ApiImplicitParam(name="type",dataType = "Integer",paramType = "query",value="试题的类型0: 选择题，1：判断题",required = false),
            @ApiImplicitParam(name="subject",dataType = "String",paramType = "query",value="试题的学科类型id,因为前端的原因，是String类型,后台会进行转换",required = false),
            @ApiImplicitParam(name="orderField",dataType = "String",paramType = "query",value="排序字段",required = false),
            @ApiImplicitParam(name="order",dataType = "Integer",paramType = "query",value="排序的顺序,默认为升序",defaultValue = "0")
    })
    public ResponseEntity<Map<String,Object>> queryAllTopics(@RequestParam(value = "key",required = false)String key,
                                                             @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                             @RequestParam(value="row",defaultValue = "5")Integer row,
                                                             @RequestParam(value="type",required = false)Integer type,
                                                             @RequestParam(value="subject",required = false)String subject,
                                                             @RequestParam(value="orderField",required = false)String orderField,
                                                             @RequestParam(value="order",defaultValue = "0")Integer order){
        PageResult<TopicBo> topics = this.topicService.queryAllTopics(key, page, row, type, subject, orderField, order);
        Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.TOPIC_OK.getValue(), "试题查询成功");
        result.put("topics",topics);
        return ResponseEntity.ok(result);
    }

    /**
     * 通过id集合获取试题集合
     * @param topics
     * @return
     */
    @PostMapping("/topic/ids")
    @ApiOperation(value="通过id集合获取试题信息",notes="通过id集合获取试题信息",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>> queryTopicsById(@RequestBody @ApiParam(value="id列表") List<String> topics){
        List<Topic> topicss=this.topicService.queryTopicsById(topics);
        System.out.println(topicss);
        Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.TOPIC_OK.getValue(), "通过id集合获取试题集合成功");
        result.put("topics",topicss);
        return ResponseEntity.ok(result);
    }

    /**
     * 新增试题
     * @param topic
     * @return
     */
    @PostMapping("/topic")
    @ApiOperation(value="新增试题",notes="新增学科信息，以json格式新增",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>> insertTopic(@RequestBody @Valid Topic topic){
        this.topicService.insertTopic(topic);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TOPIC_OK.getValue(),"试题新增成功"));
    }


    /**
     * 修改试题信息
     * @param topic
     * @return
     */
    @PutMapping("/topic")
    @ApiOperation(value="修改试题",notes="修改试题信息，以json格式修改",httpMethod = "PUT")
    public ResponseEntity<Map<String,Object>> updateTopic(@RequestBody @Valid Topic topic){
        this.topicService.updateTopic(topic);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TOPIC_OK.getValue(),"试题修改成功"));
    }

    /**
     * 删除(启用)试题
     * @param id
     * @return
     */
    @DeleteMapping("/topic/{id}")
    @ApiOperation(value="删除(启用)试题",notes = "逻辑删除",httpMethod = "DELETE")
    @ApiImplicitParam(name="id",dataType = "String",paramType = "path",value="试题的id",required = true)
    public ResponseEntity<Map<String,Object>> deleteTopic(@PathVariable("id") String id){
        this.topicService.deleteTopic(id);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TOPIC_OK.getValue(),"试题删除成功"));
    }

}
