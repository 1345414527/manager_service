package top.codekiller.manager.examination.controller.topic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.pojo.topicResult.TopicAwResult;
import top.codekiller.manager.examination.service.interfaces.topic.ITopicAnswerResultService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/15 22:57
 * @Description 试题的答题情况数据服务接口
 */
@RestController
@Api(value="试题的答题情况数据服务接口",tags = "试题的答题情况数据服务接口")
public class TopicAnswerResultController {

    @Autowired
    private ITopicAnswerResultService topicAnswerResultService;

    /**
     * 根据条件查询试题的答题情况
     * @param key     关键字(包含名称， 备注)
     * @param page    页码数
     * @param row     页行数
     * @param type    试题类型0：选择题，1：判断题
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @return
     */
    @GetMapping("/topics/result")
    @ApiOperation(value="条件分页查询所有试题",notes = "条件查询",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",dataType = "String",paramType = "query",value="关键字信息，包含id，名称， 备注",required = false),
            @ApiImplicitParam(name="page",dataType = "Long",paramType = "query",value="当前页码",defaultValue = "1"),
            @ApiImplicitParam(name="row",dataType = "Long",paramType = "query",value="当前页码的数据行数",defaultValue = "10"),
            @ApiImplicitParam(name="type",dataType = "Integer",paramType = "query",value="试题的类型0: 选择题，1：判断题",required = false),
            @ApiImplicitParam(name="subject",dataType = "String",paramType = "query",value="试题的学科类型id,因为前端的原因，是String类型,后台会进行转换",required = false),
            @ApiImplicitParam(name="orderField",dataType = "String",paramType = "query",value="排序字段",required = false),
            @ApiImplicitParam(name="order",dataType = "Integer",paramType = "query",value="排序的顺序,默认为升序",defaultValue = "0")
    })
    public ResponseEntity<Map<String,Object>> queryAllTopics(@RequestParam(value = "key",required = false)String key,
                                                             @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                             @RequestParam(value="row",defaultValue = "10")Integer row,
                                                             @RequestParam(value="type",required = false)Integer type,
                                                             @RequestParam(value="subject",required = false)String subject,
                                                             @RequestParam(value="orderField",required = false)String orderField,
                                                             @RequestParam(value="order",defaultValue = "0")Integer order){
        PageResult<TopicAwResult> topics = this.topicAnswerResultService.calculateTopicAnswerResult(key, page, row, type, subject, orderField, order);
        if(topics!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.TOPIC_OK.getValue(), "试题回答数据查询成功");
            result.put("topics",topics);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TOPIC_ERROR.getValue(),"试题回答数据查询失败"));

    }



}
