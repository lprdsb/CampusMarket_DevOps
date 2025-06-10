package code_sys.Po.Vo;

import code_sys.Po.Entity.Interaction;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 互动行为信息VO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InteractionVO extends Interaction {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 商品标题
     */
    private String productTitle;
}
