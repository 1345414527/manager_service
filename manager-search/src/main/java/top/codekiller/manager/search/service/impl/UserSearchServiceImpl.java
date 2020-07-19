package top.codekiller.manager.search.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.codekiller.manager.common.utils.JsonUtils;
import top.codekiller.manager.search.client.UserClient;
import top.codekiller.manager.search.constant.AggName;
import top.codekiller.manager.search.exception.UserDataNullException;
import top.codekiller.manager.search.pojo.UserInfo;
import top.codekiller.manager.search.pojo.common.SearchRequest;
import top.codekiller.manager.search.pojo.result.user.SearchResult;
import top.codekiller.manager.search.repository.UserInfoRepository;
import top.codekiller.manager.search.service.IUserSearchService;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.utils.DateTimeUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author codekiller
 * @date 2020/7/16 22:35
 * @Description 用户的搜索服务接口的实现
 */
@Service
public class UserSearchServiceImpl implements IUserSearchService {


    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserClient userClient;

    /**
    * @Description 构建用户数据
    * @date 2020/7/17 0:24
    * @param user
    * @return top.codekiller.manager.search.pojo.UserInfo
    */
    @Override
    public UserInfo buildUserInfo(User user) {
        if(user==null){
            throw new UserDataNullException("用于构建检索的用户数据为空");
        }

        //初始化对象
        UserInfo userInfo=new UserInfo();
        userInfo.setAge(user.getAge());
        userInfo.setAreaProvince(user.getAreaProvince());
        userInfo.setCreated(DateTimeUtils.formatLocalDateTimeToString(DateTimeUtils.convertDate2LocalDateTime(user.getCreated())));
        userInfo.setLevel(StringUtils.substring(user.getSno(),0,4));
        userInfo.setId(user.getId());
        userInfo.setImage(user.getImage());
        userInfo.setStatus(user.getStatus()?"管理大大":"普通用户");
        userInfo.setName(user.getName());
        userInfo.setUsername(user.getUsername());
        userInfo.setSno(user.getSno());

        //用空格分开，避免分词
        userInfo.setAllKey(userInfo.getUsername()+" "+userInfo.getName()+" "+userInfo.getSno());

        return userInfo;
    }

    /**
    * @Description 检索
    * @date 2020/7/17 1:36
    * @param request
    * @return top.codekiller.manager.search.pojo.result.user.SearchResult
    */
    @Override
    public SearchResult search(SearchRequest request) {
        if(StringUtils.isBlank(request.getKey())){
            return null;
        }

        //自定义查询构建器
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();

        //添加过滤查询条件
        BoolQueryBuilder baseBuilder=this.boolQueryBuilder(request);
        queryBuilder.withQuery(baseBuilder);

        //添加分页，页码从0开始
        queryBuilder.withPageable(PageRequest.of(request.getPage()-1,request.getSize()));
        //添加结果集过滤
        //queryBuilder.withSourceFilter(new FetchSourceFilter(null,new String[]{"created"}));


        //添加状态，学年，年龄，省份的聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(AggName.STATUS_AGG_NAME).field("status").size(60));
        queryBuilder.addAggregation(AggregationBuilders.terms(AggName.AGE_AGG_NAME).field("age").size(60));
        queryBuilder.addAggregation(AggregationBuilders.terms(AggName.LEVEL_AGG_NAME).field("level").size(60));
        queryBuilder.addAggregation(AggregationBuilders.terms(AggName.PROVINCE_AGG_NAME).field("areaProvince").size(60));




        //执行查询,获取结果集
        AggregatedPage<UserInfo> userPage=(AggregatedPage<UserInfo>) this.userInfoRepository.search(queryBuilder.build());
        List<String> statusAggResult = AggUtils.getStringAggResult(userPage.getAggregation(AggName.STATUS_AGG_NAME));
        List<String> areaProvinceAggResult = AggUtils.getStringAggResult(userPage.getAggregation(AggName.PROVINCE_AGG_NAME));
        List<String> levelAggResult = AggUtils.getStringAggResult(userPage.getAggregation(AggName.LEVEL_AGG_NAME));
        List<Integer> ageAggResult = AggUtils.getAgeAggResult(userPage.getAggregation(AggName.AGE_AGG_NAME));


        System.out.println("信息"+userPage.getTotalElements());
        System.out.println(userPage.getTotalPages());
        System.out.println("内容是"+userPage.getContent());
        System.out.println(statusAggResult);
        System.out.println(areaProvinceAggResult);
        System.out.println(levelAggResult);
        System.out.println(ageAggResult);
        Collections.sort(ageAggResult);
        Collections.sort(levelAggResult);
        return new SearchResult(userPage.getTotalElements(),userPage.getTotalPages(),userPage.getContent(),
                                statusAggResult,levelAggResult,ageAggResult,areaProvinceAggResult);
    }


    /**
    * @Description 构建布尔查询
    * @date 2020/7/17 1:39
    * @param request
    * @return org.elasticsearch.index.query.BoolQueryBuilder
    */
    private BoolQueryBuilder boolQueryBuilder(SearchRequest request) {
        //获取布尔查询构建器
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //给布尔查询添加基本查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("allKey",request.getKey()).operator(Operator.OR));

        // 添加过滤条件
        if (CollectionUtils.isEmpty(request.getFilter())) {
            return boolQueryBuilder;
        }

        //获取用户选择的过滤信息
        Map<String, Object> filter = request.getFilter();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery(entry.getKey(),entry.getValue()));
        }

        return boolQueryBuilder;
    }


    /**
     * @author codekiller
     * @date 2020/7/17 2:02
     * @Description 聚合静态工具类
     */
    private static class AggUtils{

        /**
         * @Description 聚合状态,学年，省份
         * @date 2020/7/17 2:02
         * @param aggregation
         * @return java.util.List<java.lang.String>
         */
        public static List<String> getStringAggResult(Aggregation aggregation){
            return ((StringTerms)aggregation).getBuckets().stream().map(bucket -> {
                return bucket.getKeyAsString();
            }).collect(Collectors.toList());
        }

        /**
        * @Description 聚合年龄
        * @date 2020/7/17 8:30
        * @param aggregation
        * @return java.util.List<java.lang.Integer>
        */
        public static List<Integer> getAgeAggResult(Aggregation aggregation){
            return ((LongTerms)aggregation).getBuckets().stream().map(bucket -> {
                return bucket.getKeyAsNumber().intValue();
            }).collect(Collectors.toList());
        }
    }

    /**
     * @Description 存储新的用户信息和更新
     * @date 2020/7/17 22:36
     * @param id
     * @return void
     */
    @Override
    public void save(Long id) {
        Map<String, Object> result = this.userClient.queryUserInfoById(id + "");
        String usersStr = JsonUtils.serialize(result.get("user"));
        User user=null;
        if(StringUtils.isNotBlank(usersStr)){
            user=JsonUtils.parse(usersStr,User.class);
        }
        //构建用户信息并保存
        UserInfo userInfo = this.buildUserInfo(user);
        this.userInfoRepository.save(userInfo);
    }


    /**
     * @Description 删除用户信息
     * @date 2020/7/17 22:36
     * @param id
     * @return void
     */
    @Override
    public void delete(Long id) {
        this.userInfoRepository.deleteById(id);
    }
}
