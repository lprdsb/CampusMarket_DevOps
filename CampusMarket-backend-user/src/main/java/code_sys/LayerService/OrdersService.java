package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.OrdersQueryDto;
import code_sys.Po.Entity.Orders;
import code_sys.Po.Vo.OrdersVO;

import java.util.List;

/**
 * 订单的业务逻辑接口
 */
public interface OrdersService {

    Result<String> save(Orders orders);

    Result<String> update(Orders orders);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<OrdersVO>> query(OrdersQueryDto ordersQueryDto);

    Result<List<OrdersVO>> queryOrdersList(OrdersQueryDto ordersQueryDto);

    Result<String> refund(Integer ordersId);

}
