package top.codekiller.manager.search.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.search.pojo.common.SearchRequest;
import top.codekiller.manager.search.pojo.result.user.SearchResult;
import top.codekiller.manager.search.service.IUserSearchService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/16 22:37
 * @Description 用户信息的检索服务查询服务接口
 */
@RestController
@Api(value="用户信息的检索服务查询服务接口",tags = "用户信息的检索服务查询服务接口")
public class UserSearchController {

    @Autowired
    private IUserSearchService userSearchService;


    /**
    * @Description 用户的检索
    * @date 2020/7/17 14:43
    * @param searchRequest
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @PostMapping("/search/userInfo")
    @ApiOperation(value="用户的检索",notes = "用于用户的基本数据检索",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>> search(@RequestBody SearchRequest searchRequest){
        System.out.println("嘻嘻"+searchRequest.getKey());
        SearchResult searchResult = this.userSearchService.search(searchRequest);
        System.out.println(searchRequest.getKey());
        if(searchResult!=null){
            Map<String, Object>  result= ControllerUtils.getPublicBackValue(CustomCode.USER_SEARCH_OK.getValue(), "用户信息检索成功");
            result.put("searchResult",searchResult);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.USER_SEARCH_ERROR.getValue(),"用户信息检索失败"));
    }



}
