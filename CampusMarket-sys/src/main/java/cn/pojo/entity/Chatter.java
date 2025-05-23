package cn.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 互动消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chatter {
    /**
     * ID
     */
    private Integer id;
    /**
     * 发者ID
     */
    private Integer senderId;
    /**
     * 收者id
     */
    private Integer receiverId;
    /**
     * 消息体
     */
    private String content;
    /**
     * 是否已读
     */
    private Boolean isRead;
    /**
     * 发送时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy年MM月dd日 HH:mm:ss")
    private LocalDateTime createTime;
}
