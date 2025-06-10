package Cn.Service;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.OrdersQueryDto;
import Cn.Poto.Entity.Orders;
import Cn.Poto.Vo.OrdersVO;

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
