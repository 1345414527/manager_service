package top.codekiller.manager.user.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.codekiller.manager.user.constant.RedisConstant;
import top.codekiller.manager.user.mapper.UserMapper;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.pojo.data.AgeData;
import top.codekiller.manager.user.pojo.data.AreaData;
import top.codekiller.manager.user.pojo.data.CreatedTimeData;
import top.codekiller.manager.user.pojo.data.UserData;
import top.codekiller.manager.user.service.IUserDataService;
import top.codekiller.manager.user.utils.DateTimeUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author codekiller
 * @date 2020/7/14 22:29
 * @Description 用户数据服务接口的实现
 */
@Service
public class UserDataServiceImpl implements IUserDataService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;


   /**
   * @Description 计算用户的统计数据,并放入缓存中。因为该查询操作太过于费时。为减少数据库的压力，24小时重新计算一次！
   * @date 2020/7/14 23:34
   * @return top.codekiller.manager.user.pojo.data.UserData
   */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserData calculateAllUserData() {
        //查看缓存
        if(redisTemplate.hasKey(RedisConstant.STATISTIC_DATA_NAME)){
            return (UserData) this.redisTemplate.opsForValue().get(RedisConstant.STATISTIC_DATA_NAME);
        }

        List<User> users=this.userMapper.queryStatisticData();

        List<AreaData> areaData=new ArrayList<>();
        List<AgeData> ageData=new ArrayList<>();
        List<CreatedTimeData> createdTimeData=new ArrayList<>();
        for (User user : users) {
            this.calculateProvinceData(user.getAreaProvince(),areaData);
            this.calculateAgeData(user.getAge(),ageData);
            this.calculateCreatedTimeData(user,createdTimeData);
        }
        Collections.sort(ageData);
        Collections.sort(createdTimeData);

        //只需要六条数据
        createdTimeData=createdTimeData.size()<=6?createdTimeData:new ArrayList(createdTimeData.subList(0,6));
        this.calculateAverAge(createdTimeData);

        UserData userData=new UserData(areaData,ageData,createdTimeData);

        //放入缓存,并设置24小时的过期时间
        if(!redisTemplate.hasKey(RedisConstant.STATISTIC_DATA_NAME)){
             this.redisTemplate.opsForValue().set(RedisConstant.STATISTIC_DATA_NAME,userData,RedisConstant.STATISTIC_DATA_TIMEOUT, TimeUnit.HOURS);
        }

        System.out.println("查询的信息为"+userData);

        return userData;
    }




    /**
    * @Description 计算省份数据
    * @date 2020/7/14 23:50
    * @param province
    * @param areaDatas
    * @return void
    */
    private void  calculateProvinceData(String province,List<AreaData> areaDatas){
        if(StringUtils.isBlank(province)){
            return;
        }

        for (AreaData areaData : areaDatas) {
            if (areaData.getProvince().equalsIgnoreCase(province)){
                areaData.userNumIncrease();
                return;
            }
        }

        //新增数据
        areaDatas.add(new AreaData(province,1L));
    }

    /**
    * @Description 计算年龄数据
    * @date 2020/7/15 0:05
    * @param age
    * @param ageDatas
    * @return void
    */
    private void calculateAgeData(Integer age, List<AgeData> ageDatas){

        for (AgeData ageData : ageDatas) {
            if(ageData.getAge().equals(age)){
                ageData.userNumIncrease();
                return ;
            }
        }
        //新增数据
        ageDatas.add(new AgeData(age,1L));
    }

    /**
    * @Description 计算创建日期相关数据
    * @date 2020/7/15 0:11
    * @param user
    * @param createdTimeDatas
    * @return void
    */
    private void calculateCreatedTimeData(User user, List<CreatedTimeData> createdTimeDatas) {
        //日期转换
        Instant instant = user.getCreated().toInstant();
        LocalDateTime created=instant.atOffset(ZoneOffset.of("+8")).toLocalDateTime();
        int month=created.getMonthValue();

        for (CreatedTimeData createdTimeData : createdTimeDatas) {
            if(createdTimeData.getCreated().getMonthValue()==month){
                createdTimeData.userNumIncrease();
                createdTimeData.totalAgeIncrease(user.getAge());
                return;
            }
        }

        //新增数据
        createdTimeDatas.add(new CreatedTimeData(created,month, DateTimeUtils.mapperMonthByNumber(month,true),
                                                (long)user.getAge(),0,1L));
    }


    /**
    * @Description 计算平均年龄
    * @date 2020/7/15 0:47
    * @param createdTimeDatas
    * @return void
    */
    private void calculateAverAge(List<CreatedTimeData> createdTimeDatas){
        createdTimeDatas.forEach(createdTimeData->{
            createdTimeData.setAverAge((int) (createdTimeData.getTotalAge()/createdTimeData.getUserNum()));
        });
    }
}
