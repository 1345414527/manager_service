package top.codekiller.manager.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.codekiller.manager.user.pojo.User;

import java.util.List;

/**
 * 用户的数据库操作类
 * @author codekiller
 * @date 2020/5/20 17:08
 */
public interface UserMapper  extends BaseMapper<User> {

    @Select("select * from tb_user")
    List<User> queryAllUsers();

    /**
    * @Description 查询统计数据(创建时间，年龄，省区)
    * @date 2020/7/14 23:17
    * @return java.util.List<top.codekiller.manager.user.pojo.User>
    */
    @Select("select `created` ,`age`,`area_province` as areaProvince from `tb_user` where deleted=0 order by `created` desc")
    List<User> queryStatisticData();
}
