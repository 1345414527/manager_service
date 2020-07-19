package top.codekiller.manager.upload.upload.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.upload.upload.service.IUploadService;

import java.util.Map;

/**
 *
 * @author codekiller
 * @date 2020/5/21 17:40
 * 用于用户的图片文件上传
 */
@RestController
@RequestMapping("upload")
@Api(value="用户上传服务接口",tags = "用户上传服务接口")
public class UploadController {

    @Autowired
    IUploadService uploadService;



   /**
   * @Description 上传头像
   * @date 2020/5/21 17:41
   * @param file
   * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
   */
    @PostMapping("/image")
    @ApiOperation(value="用户的头像上传",notes = "仅仅上传头像，并且上传之后自动根据用户信息更新数据库，返回地址",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>>  uploadImage(@RequestParam("file")MultipartFile file){
        String url=uploadService.uploadImage(file);
        if(StringUtils.isBlank(url)){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"图片上传错误"));
        }
        Map<String, Object> info = ControllerUtils.getPublicBackValue(HttpStatus.ACCEPTED.value(), "图片上传成功");
        info.put("image",url);
        return ResponseEntity.ok(info);
    }

    /**
     * @Description 上传图片
     * @date 2020/5/21 17:41
     * @param file
     * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @PostMapping("/image2")
    @ApiOperation(value="用户的图片上传",notes = "上传用户的图片,并获取图片地址",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>>  uploadImage2(@RequestParam("file")MultipartFile file){
        String url=uploadService.uploadImage2(file);
        if(StringUtils.isBlank(url)){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"图片上传错误"));
        }
        Map<String, Object> info = ControllerUtils.getPublicBackValue(HttpStatus.ACCEPTED.value(), "图片上传成功");
        info.put("image",url);
        return ResponseEntity.ok(info);
    }




}
