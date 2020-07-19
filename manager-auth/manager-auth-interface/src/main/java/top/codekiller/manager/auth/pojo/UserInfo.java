package top.codekiller.manager.auth.pojo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于jwt生成token的数据
 * @author codekiller
 * @date 2020/5/20 18:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    /**
     * 用于验证的id
     */
    private Long id;

    /**
     * 用于验证的用户名
     */
    private String username;


}
