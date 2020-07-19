## 前台代码

&emsp;:point_right:  [manager-protal](https://github.com/1345414527/manager_protal)

<br/>



## 视频演示



&emsp;:point_right:[b站视频](https://www.bilibili.com/video/bv13C4y1b7NS)



## 项目基本功能

&emsp;本在线考试系统主要完成了

1. 用户注册，根据用户名和密码实现注册
2. 用户登录，分为账号密码登录，短信登录，邮箱登录
3. 用户信息完善，用户基本信息填写(头像，名称，年龄，地区，学号，手机号，邮箱号)。
4. 用户信息搜索，根据输入的关键字进行信息检索，查看用户的数据。可以通过聚合条件检索。管理员可以查看完整数据和统计信息。
5. 用户管理，管理员可以查看用户的相关数据，并进行管理员的分配。
6. 考生管理，管理员考生考试的管理，可以查看考生考试的信息，考生试卷，重置试卷，再次考试，无条件删除考生订阅信息。
7. 学科管理，管理员可以查看所有学科信息，添加学科，修改学科信息，删除(是否启用)学科。
8. 试题管理，管理员可以查看所有试题信息，添加试题，修改试题信息，删除(是否启用)学科。
9. 试卷管理，管理员可以查看所有试卷信息，添加试卷，修改试卷信息，删除(是否启用)试卷。可以预览试卷，并且可以选择是否发布试卷。
10. 考试管理，发布好的试卷会进入考试管理，管理员可以为试卷设置考试试卷，取消试卷发布和预览试卷。
11. 答题管理，管理员可以查看每个试题的答题情况，并进行答题情况统计(答题人数，答题总数，错误率)
12. 考试订阅，考生可以查看已经发布了的考试，可以订阅已发布并且设置了考试时间的考试，并且有条件的删除订阅
13. 在线考试答题，考生可以查看已订阅的考试信息，答题次数，并且进行答题，考完之后可以查看试卷和分数，有条件的删除订阅。
14. 成绩查询，考生可以查看订阅试卷的基本信息，分数以及答题次数。
15. 单个考生的数据统计，考生可以查看自己不同学科的近6次考试情况的统计，考试试卷分布图，学科考试次数比例图，不同学科的答题数。
16. 全部考生的数据统计，管理员可以查看全部考生不同学科的近20次考试情况的统计，考试试卷分布图，学科考试次数比例图，不同学科的答题数。
17. 用户数据统计，管理员可以查看全部用户的地理分布情况，不同年龄的用户数量分布，近6个月的用户注册数和平均年龄。
18. 日志管理，管理员可以查看关于考试的日志记录。






<br/>
<br>


## 系统架构
&emsp;在线考试系统`主要采用`Vue+SpringBoot+SpringCloud+Mybatis框架开发。内部采用标准的MVC架构进行基本框架搭建。通过Ngnix进行反向代理，服务器采用Docker进行统一管理，使用FastDFS完成远程的文件上传。具体的使用技术请看:point_right:[技术选型](#techSelect)



![](https://imgconvert.csdnimg.cn/aHR0cHM6Ly95YW54dWFuLm5vc2RuLjEyNy5uZXQvMTIyYTA1ZDc3ZmFiNjJmZDRhYjdmZWRhMDkyYjQxYTUuanBn?x-oss-process=image/format,png)

<br/>
<br/>

<span id="techSelect"></span>
<br/>



## 技术选型
### 前台

| 技术             | 介绍                              |
| ---------------- | --------------------------------- |
| HTML,CSS,LESS    | emm...没什么好说的                |
| Vue.js2.6        | 项目的前台是完全基于Vue进行搭建的 |
| Npm              | 前端安装包工具                    |
| Webpack          | 前端模块打包工具                  |
| Vue-cli          | Vue的脚手架，用于构建基本项目架构 |
| Vue-router       | Vue的路由工具                     |
| Vuex             | Vue的状态管理模式，集中式存储管理 |
| Element-ui       | Vue的一些基本组件库               |
| axios            | ajax的框架，用于异步请求          |
| v-charts         | 构建统计视图                      |
| vue-quill-editor | 基于Vue的富文本框架               |
| vue-particles    | 粒子特效                          |

<br/>



### 后台

|                     | 介绍                                                         |
| ------------------- | ------------------------------------------------------------ |
| SpringBoot          | 该项目每个微服务内部都是使用SpringBoot进行搭建的，emmm，直接牛逼 |
| SpringCloud         | 该项目是由好几个微服务组成的，微服务之间的注册和调用等是通过SpringCloud来完成的。使用到了Eureka，Zuul，Ribbon，Feign |
| MybatisPlus         | 该项目使用MybatisPlus来完成对mysql的持久层操作               |
| SpringData          | 该项目虽然没有使用JPA来完成对mysql的操作，但是其他数据库(MongoDB，redis，ElasticSearch)都是使用SpringData来操作的 |
| JWT                 | 该项目用jwt实现单点登录，对用户的请求进行认证。采用的是无状态登录 |
| Rsa                 | 一个非对称机密算法，将token的载荷和秘钥进行加密放入签名域    |
| FastDFS             | 一个轻量级的分布式文件系统，用于项目上传图片等文件           |
| RabbitMQ            | 该技术是基于AMQP协议的消息代理软件，通过该技术实现了手机，验证码的发送以及数据库之间数据的同步 |
| Mysql               | 该项目用Mysql来存储主要的数据(用户信息，学科信息，发布的试卷信息，用户订阅的考试信息，用户的考试亲狂) |
| MD5                 | 一个不可逆加密算法，该项目用md5来实现对用户密码的加密        |
| Druid               | 该项目使用Druid来作为mysql的数据源                           |
| MongoDB             | 该项目用MongoDB来存储关于试卷的数据(试题信息，试卷信息)以及日志信息 |
| ElasticSearch       | 该项目用ElasticSearch来存储用于搜索的用户数据，并实现搜索和聚合等功能 |
| Redis               | 该项目用Redis来做部分数据的缓存，并且用redis来存储手机和邮箱的验证码信息 |
| Nginx               | 该项目用Nginx来实现反向代理                                  |
| Quartz              | 定时任务框架，该项目用Quartz来实现某些操作的定               |
| Swagger2            | 该项目用swagger2实现对RESTful风格的api进行统一描述和可视化调用 |
| Lombok              | 该项目使用Lombok来简化实体类和日志                           |
| Logback             | 该项目使用logback来实现日志的输出和持久化                    |
| Hibernate-validator | 该项目使用hibernate-validator来进行部分实体类的数据校验      |
| Docker              | 该项目使用的服务器是用docker进行统一管理的                   |
| 阿里云短信服务服务  | 该项目使用的短信服务是由阿里云提供的                         |
| Git                 | 该项目用git来进行版本管理                                    |



> 其实，我这个项目不应该使用JWT完成单点登录的，最好是==使用SpringSecurity和OAuth2==来完成权限和登录的控制，一开始对项目的整体预估不足，贪图简单，就直接使用了JWT+Rsa来写了，写到后来权限控制那块很难控制了，无奈呀~。没办法，都成型了，也懒得重构了，这个项目就这样吧。下次注意！



![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719095908918.png)



<br/>

<br/>



## 项目目录框架

### 前台

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719095927780.png)

<br/>



### 后台

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719095942531.png)



<br/>



## 单微服务目录框架

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719095958507.png)



> 这里已考试微服务为例



## 用户微服务介绍

<span id="user_table"></span>

### 数据库

```java
CREATE TABLE `tb_user` (
    `id` bigint(64) NOT NULL COMMENT '雪花算法生成id',
    `name` varchar(10) DEFAULT NULL COMMENT '用户的名称',
    `age` int(3) DEFAULT '0' COMMENT '用户的年龄',
    `area_province` varchar(10) DEFAULT NULL COMMENT '用户的地区-省',
    `area_city` varchar(10) DEFAULT NULL COMMENT '用户的地区-市',
    `area_county` varchar(10) DEFAULT NULL COMMENT '用户的地区-县',
    `status` tinyint(1) NOT NULL COMMENT '是否为管理员，1是，0不是',
    `username` varchar(32) NOT NULL COMMENT '用户名',
    `sno` varchar(32) DEFAULT NULL COMMENT '用户的学号',
    `password` varchar(32) NOT NULL COMMENT '密码，加密存储',
    `phone` varchar(11) DEFAULT NULL COMMENT '用户的手机号',
    `email` varchar(50) DEFAULT NULL COMMENT '用户的邮箱',
    `image` varchar(100) DEFAULT NULL COMMENT '用户的头像地址',
    `created` datetime NOT NULL COMMENT '创建时间',
    `salt` varchar(32) NOT NULL COMMENT '密码加密的salt值',
    `version` bigint(20) DEFAULT '0' COMMENT '版本，乐观锁',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除，1删除，0没删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
```



### API



![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100021896.png)



<br/>



### 手机，邮箱验证码获取接口

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100035771.png)

&emsp;这里就两个接口，一个是获取手机验证码，一个是获取邮箱验证码

- 手机验证码通过rabbitmq发送验证码，并且设置2分钟的过期时期保存在redis中

  ```java
  //发送消息
  this.amqpTemplate.convertAndSend(this.authCodeProperties.getExchangeName(), "authCode.phone", authInfo);
  
  //将验证码放入redis中
  this.redisTemplate.opsForValue().set(this.authCodeProperties.getPhoneName()+phone,authcode,2, TimeUnit.MINUTES);
  ```

- 邮箱验证码通过rabbitmq发送验证码，并且设置2分钟的过期时期保存在redis中

  ```java
  //发送消息
  this.amqpTemplate.convertAndSend(this.authCodeProperties.getExchangeName(), "authCode.email", authInfo);
  
  //将验证码放入redis中
  this.redisTemplate.opsForValue().set(this.authCodeProperties.getEmailName()+email,authcode,2, TimeUnit.MINUTES);
  ```



&emsp;:point_right: [点击查看rabbitmq接受消息代码](#rabbitmq)



### 用户基本的DML操作服务接口

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100048732.png)



&emsp;DML操作无非就是赠删改操作，但是看我们的API接口却并==没有DELETE的操作==，这是为什么呢？

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100058709.png)

&emsp;仔细看我们的[用户数据库表](#user_table),我是使用的==逻辑删除==！







> 剩下的接口就不说了



<br/>



## 考试微服务介绍

### 数据库

#### 学科表

```sql
CREATE TABLE `tb_subject` (
    `id` bigint(64) NOT NULL COMMENT '雪花算法生成id',
    `name` varchar(10) DEFAULT NULL COMMENT '学科的名称',
    `note` varchar(100) DEFAULT NULL COMMENT '学科的备注信息',
    `icon` varchar(50) NOT NULL COMMENT '学科的图标',
    `index` varchar(100) NOT NULL COMMENT '学科的前台路径',
    `created` datetime NOT NULL COMMENT '创建时间',
    `version` bigint(20) DEFAULT '0' COMMENT '版本，乐观锁',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除，1删除，0没删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学科表';
```





#### 试题表

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100111216.png)

> 试题的信息是存放在MongoDB中的

- select：是用于存储选择题的选项的，判断题不存在
- answer：试题的正确答案索引
- type：试题的类型，0：选择题，1：判断题
- subject：学科的id
- note：试题的备注信息



<br/>



#### 试卷表

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100127333.png)

> 试卷的信息也是存放在MongoDB中的

- name: 试卷名字
- subject：试卷的学科id
- school：出题学校的名字
- creatoe：出题人的用户名（用户名不可变）
- astrict：试卷的答题限制时间
- select：选择题的题目id
- judge：判断题的题目id
- selectScore：每到选择题的分数
- judgeScore：每到判断题的分数
- note：备注信息
- publicsh：是否发布



<br/>



#### 发布试卷记录表

```sql
CREATE TABLE `tb_public_test` (
    `id` bigint(64) NOT NULL COMMENT '雪花算法生成id',
    `test_id` varchar(100) NOT NULL COMMENT '试卷的id',
    `start_time` timestamp NOT NULL DEFAULT '2020-01-01 00:00:00' COMMENT '试卷的开始时间',
    `end_time` timestamp NOT NULL DEFAULT '2020-01-01 00:00:00' COMMENT '试卷的结束时间',
    `status` int(3) NOT NULL COMMENT '试卷的状态,-2:已删除,-1：初始化,0:未开始,1:开启中,2:已结束',
    `created` datetime NOT NULL COMMENT '创建时间',
    `version` bigint(20) DEFAULT '0' COMMENT '版本，乐观锁',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除，1删除，0没删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试卷发布状态表';
```



> 我给start_time和end_time设置了一个过去的初始化时间



#### 用户订阅试卷表

```sql
CREATE TABLE `tb_subscribe_exam` (
    `id` bigint(64) NOT NULL COMMENT '雪花算法生成id',
    `user_id` bigint(64) NOT NULL COMMENT '订阅的用户id',
    `test_id` varchar(100) NOT NULL COMMENT '试卷的id',
    `status` int(3) DEFAULT '0' COMMENT '订阅记录的状态0:未考试,1:正在考试,2:已考试,3:再次考试',
    `score` double(6,1) DEFAULT '0.0' COMMENT '试卷的分数',
    `begin_work_time` timestamp NOT NULL DEFAULT '2020-01-01 00:00:00' COMMENT '开始答题时间',
    `finish_work_time` timestamp NOT NULL DEFAULT '2020-01-01 00:00:00' COMMENT '结束答题时间',
    `frequency` int(3) DEFAULT '0' COMMENT '考试的次数',
    `created` datetime NOT NULL COMMENT '创建时间',
    `version` bigint(20) DEFAULT '0' COMMENT '版本，乐观锁',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除，1删除(取消订阅后的状态)，0没删除(点击订阅后的状态)',
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `tb_subscribe_exam_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户订阅试卷表';
```



<br/>





#### 试题答题情况表

```sql
CREATE TABLE `tb_exam_answer_situation` (
    `id` bigint(64) NOT NULL COMMENT '雪花算法生成id',
    `subscribe_exam_id` bigint(64) NOT NULL COMMENT '订阅的考试id',
    `topic_id` varchar(100) NOT NULL COMMENT '试题id',
    `user_answer` varchar(200) NOT NULL COMMENT '用户的答案,-1为未答题',
    `answer_situation` int(3) NOT NULL COMMENT '用户的答题情况。-1:未答题,0：答错,1：答对',
    `score` double(6,1) DEFAULT '0.0' COMMENT '试题的得分',
    `created` datetime NOT NULL COMMENT '创建时间',
    `version` bigint(20) DEFAULT '0' COMMENT '版本，乐观锁',
    `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除，1删除，0没删除',
    PRIMARY KEY (`id`),
    KEY `subscribe_exam_id` (`subscribe_exam_id`),
    CONSTRAINT `tb_exam_answer_situation_ibfk_1` FOREIGN KEY (`subscribe_exam_id`) REFERENCES `tb_subscribe_exam` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户答题具体情况';
```



> 用户每一个答题都会生成一条记录，与订阅试卷表示一对多的关系



<br/>



#### 日志记录表

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100145397.png)

> 用于记录用户，管理员的一些试卷操作。(比如：答题，取消试卷订阅，重置试卷等)



### API

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100157910.png)





> Api以及具体的实现代码太多了，就不多说了





<span id="rabbitmq"></span>

## 消息服务介绍

> 以手机验证码为例

```java
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value="MANAGER_PHONE_SMS_QUEUE",durable = "true"),
    exchange = @Exchange(value="MANAGER_EXCHANGE_SMS",ignoreDeclarationExceptions = "true",type=ExchangeTypes.TOPIC),
    key = "authCode.phone"
))
public void sendPhoneAuthCode(Map<String,String> msg) throws ClientException {
    if(CollectionUtils.isEmpty(msg)){
        return ;
    }
    String phone = msg.get("phone");
    String authcode = msg.get("authcode");

    //放弃处理
    if(StringUtils.isAllBlank(phone,authcode)){
        return ;
    }
    log.info("接收到 {} 的验证码 {}，准备发送",phone,authcode);

    if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(authcode)) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("authcode", authcode);
        this.sendPhoneSmsUtils.sendSms(phone,jsonObject.toString(),this.smsProperties.getSignName(),this.smsProperties.getVerifyCodeTemplate());
    }
}
```



- 当接收到数据时，会对数据进行一个简单的空判断，复杂的判断在前台和传递数据的时候已经校验过了
- 数据没问题，就会调用阿里云的手机验证码服务，对对应的手机号发送验证码







<br/>



## 上传微服务



- 上传图像回显url，并修改数据库

```java
public String uploadImage(MultipartFile file) {
    String originName=file.getOriginalFilename();
    //验证文件类型
    String contentType=file.getContentType();
    if(!uploadProperties.getContentTypes().contains(contentType)){
        //使用日志记录不合法的信息
        log.info("文件类型不合法: {}",originName);
        return null;
    }

    try {
        //校验文件的内容
        BufferedImage bufferedImage= ImageIO.read(file.getInputStream());
        if(bufferedImage==null){
            log.info("文件的内容不合法: {}",originName);
            return null;
        }
        //获取文件类型
        String suffix=StringUtils.substringAfterLast(originName,".");
		
        //保存到服务器
        StorePath storePath=fastFileStorageClient.uploadFile(file.getInputStream(),file.getSize(),suffix,null);
        //返回url，进行回显
        String url=uploadProperties.getImageUrl()+storePath.getFullPath();
        log.info("上传成功: {},带分组路径: {}",originName,url);

        //修改用户信息
        this.userClient.updateImage(url);
        return url;
    } catch (IOException e){
        log.info("服务器内部错误,图片上传失败:{}",originName);
        e.printStackTrace();
    }

    return null;

}
```



<br/>



- 上传缩略图

```java
StorePath storePath=this.fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(),file.getSize(),suffix,null);

String thumbImagePath =uploadProperties.getImageUrl()+storePath.getGroup()+"/"+thumbImageConfig.getThumbImagePath(storePath.getPath());
```





<br/>





## 搜索微服务

### API

![](C:\Users\MyPC\AppData\Roaming\Typora\typora-user-images\image-20200719100208523.png)

> 加一个搜索查询API



<br/>



### 监听

```java
/**
    * @Description 接受新增和修改用户信息的消息
    * @date 2020/7/17 23:30
    * @param id
    * @return void
    */
@RabbitListener(bindings = @QueueBinding(
    value=@Queue(value="MANAGER.SEARCH.SAVE.QUEUE",durable = "true"),
    exchange = @Exchange(value="MANAGER.EXCANGE.USER.SEARCH"
                         ,ignoreDeclarationExceptions = "true"
                         ,type = ExchangeTypes.TOPIC),
    key = {"user.insert","user.update"}
))
public void save(Long id){
    if(id==null){
        throw  new NullPointerException("新增(更新)检索用户信息的id为空");
    }
    this.userSearchService.save(id);
}

/**
    * @Description 接受删除用户信息的消息
    * @date 2020/7/17 23:31
    * @param id
    * @return void
    */
@RabbitListener(bindings = @QueueBinding(
    value=@Queue(value="MANAGER.SEARCH.DELETE.QUEUE",durable = "true"),
    exchange = @Exchange(value="MANAGER.EXCANGE.USER.SEARCH"
                         ,ignoreDeclarationExceptions = "true"
                         ,type = ExchangeTypes.TOPIC),
    key = {"item.delete"}
))
public void delete(Long id){
    if(id==null){
        throw  new NullPointerException("删除检索用户信息的id为空");
    }
    this.userSearchService.delete(id);
}
```



> 当新增，修改，删除用户时，会通知ElasticSearch进行数据修改，是数据库信息同步



<br/>



### 服务

```java
/**
 * @author codekiller
 * @date 2020/7/16 20:17
 * @Description 用户的搜索服务接口
 */
public interface IUserSearchService {


    /**
    * @Description 构建用户数据
    * @date 2020/7/17 20:18
    * @param user
    * @return top.codekiller.manager.search.pojo.UserInfo
    */
    UserInfo buildUserInfo(User user);


    /**
    * @Description 检索
    * @date 2020/7/17 21:20
    * @param searchRequest
    * @return top.codekiller.manager.search.pojo.result.user.SearchResult
    */
    SearchResult search(SearchRequest searchRequest);


    /**
    * @Description 存储新的用户信息和更新
    * @date 2020/7/17 22:36
    * @param id
    * @return void
    */
    void save(Long id);

    /**
    * @Description 删除用户信息
    * @date 2020/7/17 22:36
    * @param id
    * @return void
    */
    void delete(Long id);
}
```



> 一共四个服务，就是增删改查！





> ps:
>
> &emsp;项目有很多的不足，有很多都是应该好好完善的和修改的，我也懒得再去修改和重构了。因为是第一次完全靠自己写一个项目，很多规范一开始做的不是很好。
>
> &emsp;当在写上传和用户等微服务时，很多东西没有注意到，比如就说权限管理那块；还有状态码相关，一开始全部用的自带状态码，用来用去发现就那几个...:unamused:,后来大部分用得都是自建状态码。还有异常处理，实在懒得去做太多处理了，一个字：就是懒！封装做的也不是太好！
>
> &emsp;索性在写试卷微服务时，有了一定的改善，代码规范稍微好了一点，也有了一定的套路。但还是有很大的不足。
>
> &emsp;怎么说呢，因为有期末考试的缘故，也花了好长时间去复习，所以断断续续做了一个多月。习惯了写后端，这个前台也确实花了我不少时间，也是第一次完全用Vue去构建前台，虽然也有些不规范，并且很多功能没有去添加。但是做下来，也算有所收获吧。那就不亏！
>
> &emsp;不知道下次要多久再去构建这么一个完整项目了，今年要多学些技术，明年就要考研了，一切都要时间，都需要去慢慢磨。这是一次不算很好，但也绝对不糟糕的体验，以后会继续努力！