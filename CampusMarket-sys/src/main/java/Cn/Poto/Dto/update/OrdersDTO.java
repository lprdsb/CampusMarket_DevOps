package Cn.Poto.Dto.update;

import Cn.Poto.Entity.Orders;
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
