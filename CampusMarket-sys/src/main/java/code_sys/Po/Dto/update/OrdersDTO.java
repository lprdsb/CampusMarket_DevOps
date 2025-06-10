package code_sys.Po.Dto.update;

import code_sys.Po.Entity.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单的DTO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrdersDTO extends Orders {
    /**
     * 下单多少件
     */
    private Integer buyNumber;
}
