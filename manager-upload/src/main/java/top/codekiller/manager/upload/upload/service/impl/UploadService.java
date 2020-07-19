package top.codekiller.manager.upload.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.codekiller.manager.upload.client.UserClient;
import top.codekiller.manager.upload.upload.properties.UploadProperties;
import top.codekiller.manager.upload.upload.service.IUploadService;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;


@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
@Service
public class UploadService implements IUploadService {

    private UploadProperties uploadProperties;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    @Autowired
    private UserClient userClient;


    //构造器注入properties
    public UploadService(UploadProperties uploadProperties){
        this.uploadProperties=uploadProperties;
    }


    /**
     * 上传头像
     * @param file
     * @return
     */
    @Override
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
//            file.transferTo(new File(uploadProperties.getSavePath()+id+"."+suffix));
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

    /**
     * 上传图片
     * @param file
     * @return
     */
    @Override
    public String uploadImage2(MultipartFile file) {
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

//            StorePath storePath=fastFileStorageClient.uploadFile(file.getInputStream(),file.getSize(),suffix,null);

            //缩略图
            StorePath storePath=this.fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(),file.getSize(),suffix,null);
            //返回url，进行回显
//            String url=uploadProperties.getImageUrl()+storePath.getFullPath();

            String thumbImagePath =uploadProperties.getImageUrl()+storePath.getGroup()+"/"+thumbImageConfig.getThumbImagePath(storePath.getPath());
            System.out.println("缩略图"+thumbImagePath);

            log.info("上传成功: {},带分组路径: {}",originName,thumbImagePath);
            return thumbImagePath;
        } catch (IOException e){
            log.info("服务器内部错误,图片上传失败:{}",originName);
            e.printStackTrace();
        }

        return null;

    }
}
