package top.codekiller.manager.examination.pojo.topicResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author codekiller
 * @date 2020/7/16 0:41
 * @Description 用户试题回答的情况
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicAnswerSituation implements Comparable<TopicAnswerSituation>{
    /**
     * 试题回答情况
     */
    private String answer;


    /**
     * 该答案回答的人数
     */
    private Long count;


    /**
    * @Description 以count从大到小排序
    * @date 2020/7/16 0:46
    * @param o
    * @return int
    */
    @Override
    public int compareTo(TopicAnswerSituation o) {
        return (int) (o.getCount()-this.getCount());
    }


    public void countIncrese(){
        ++this.count;
    }
}
