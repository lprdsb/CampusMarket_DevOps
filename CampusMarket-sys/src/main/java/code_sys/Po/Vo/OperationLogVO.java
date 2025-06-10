package code_sys.Po.Vo;

import code_sys.Po.Entity.OperationLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志出参类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLogVO extends OperationLog {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户账号
     */
    private String userAccount;
}
