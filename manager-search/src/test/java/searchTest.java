import com.netflix.discovery.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.common.utils.JsonUtils;
import top.codekiller.manager.search.ManagerSearchApplication;
import top.codekiller.manager.search.client.UserClient;
import top.codekiller.manager.search.pojo.UserInfo;
import top.codekiller.manager.search.pojo.common.SearchRequest;
import top.codekiller.manager.search.pojo.result.user.SearchResult;
import top.codekiller.manager.search.repository.UserInfoRepository;
import top.codekiller.manager.search.service.IUserSearchService;
import top.codekiller.manager.user.pojo.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author codekiller
 * @date 2020/7/17 0:53
 * @Description TODO
 */

@SpringBootTest(classes = ManagerSearchApplication.class)
@RunWith(value = SpringRunner.class)
public class searchTest {

    @Autowired
    private UserClient userClient;

    @Autowired
    private IUserSearchService userSearchService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;



    /**
    * @Description 新增初始数据
    * @date 2020/7/17 1:17
    * @return void
    */
    @Test
    public void insertInitData(){
        //创建索引和映射
        this.elasticsearchTemplate.createIndex(UserInfo.class);
        this.elasticsearchTemplate.putMapping(UserInfo.class);
        Map<String, Object> map = this.userClient.queryAllUsers();
        String usersStr = JsonUtils.serialize(map.get("users"));
        List<User> users=null;
        if(StringUtils.isNotBlank(usersStr)){
            users=JsonUtils.parseList(usersStr,User.class);
        }

        List<UserInfo> userInfos = users.stream().map(user -> {
            try {
                return this.userSearchService.buildUserInfo(user);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        this.userInfoRepository.saveAll(userInfos);
    }

    @Test
    public void testSearch(){
        SearchResult code = this.userSearchService.search(new SearchRequest("小莫", 1, null));
        System.out.println(code);
    }


    /**
    * @Description 测试新增方法
    * @date 2020/7/17 1:17
    * @return void
    */
    @Test
    public void test(){
        Map<String, Object> map = this.userClient.queryUserInfoByUsername("codekiller");
        String userStr = JsonUtils.serialize(map.get("user"));
        System.out.println("字符串"+userStr);
        User user=null;
        if(StringUtils.isNotBlank(userStr)){
            user= JsonUtils.parse(userStr, User.class);
        }
        System.out.println("用户信息"+user);
        UserInfo userInfo = this.userSearchService.buildUserInfo(user);
        System.out.println("检索用户信息"+userInfo);
    }
}
