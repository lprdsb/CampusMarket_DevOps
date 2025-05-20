package cn.pojo.vo;

import cn.pojo.entity.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息出参VO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageVO extends Message {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户头像
     */
    private String userAvatar;

}
