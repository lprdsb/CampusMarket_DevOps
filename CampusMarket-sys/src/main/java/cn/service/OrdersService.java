package cn.service;

import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.CategoryQueryDto;
import cn.pojo.dto.query.extend.OrdersQueryDto;
import cn.pojo.entity.Category;
import cn.pojo.entity.Orders;
import cn.pojo.vo.OrdersVO;

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

    Result<String> returnMoney(Integer ordersId);

}