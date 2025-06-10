package code_sys.Po.Vo;

import code_sys.Po.Entity.Chatter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatterVO extends Chatter {
    private String senderName;

    private String senderAccount;

    private String senderAvatar;

    private String receiverName;

    private String receiverAccount;

    private String receiverAvatar;
}
