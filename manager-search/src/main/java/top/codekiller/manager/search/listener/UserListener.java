package top.codekiller.manager.search.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.codekiller.manager.search.service.IUserSearchService;

/**
 * @author codekiller
 * @date 2020/7/17 23:13
 * @Description 接受用户信息新增，删除，更新操作的id信息
 */
@Component
public class UserListener {

    @Autowired
    private IUserSearchService userSearchService;


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


}
