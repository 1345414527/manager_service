package top.codekiller.manager.upload.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

    /**
     * 上传头像
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file);

    /**
     * 上传图片
     * @param file
     * @return
     */
    String uploadImage2(MultipartFile file);
}
