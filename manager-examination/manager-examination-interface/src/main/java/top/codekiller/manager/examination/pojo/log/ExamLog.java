package top.codekiller.manager.examination.pojo.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author codekiller
 * @date 2020/7/16 15:40
 * @Description 考试日志记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tb_exam_log")
public class ExamLog {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 信息
     */
    private String info;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime create;


}
