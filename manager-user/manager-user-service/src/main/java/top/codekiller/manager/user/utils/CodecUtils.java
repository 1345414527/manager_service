package top.codekiller.manager.user.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;


/**
 * md5算法工具类
 * @author codekiller
 * @date 2020/5/20 20:26
 */
public class CodecUtils {



    public static String md5Hex(String data,String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.md5Hex(salt + DigestUtils.md5Hex(data));
    }

    public static String shaHex(String data, String salt) {
        if (StringUtils.isBlank(salt)) {
            salt = data.hashCode() + "";
        }
        return DigestUtils.sha512Hex(salt + DigestUtils.sha512Hex(data));
    }

    public static String generateSalt(){
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }
}
