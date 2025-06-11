package code_sys.LayerControl;

import code_sys.Aop.Pager;
import code_sys.Interceptor.LocalThreadHolder;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.OrdersQueryDto;
import code_sys.Po.Entity.Orders;
import code_sys.Po.Vo.OrdersVO;
import code_sys.LayerService.OrdersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/orders")
public class LayerOrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 新增
     *
     * @param orders 参数
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody Orders orders) {
        return ordersService.save(orders);
    }

    /**
     * 修改
     *
     * @param orders 参数
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    public Result<String> update(@RequestBody Orders orders) {
        return ordersService.update(orders);
    }

    /**
     * 批量删除
     */
    @PostMapping(value = "/batchDelete")
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return ordersService.batchDelete(ids);
    }

    /**
     * 查询用户自己名下的订单数据
     *
     * @param ordersQueryDto 查询参数
     * @return Result<List < OrdersVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    public Result<List<OrdersVO>> queryUser(@RequestBody OrdersQueryDto ordersQueryDto) {
        ordersQueryDto.setUserId(LocalThreadHolder.getUserId());
        return ordersService.query(ordersQueryDto);
    }

    /**
     * 查询用户发布的商品产生的订单数据
     *
     * @param ordersQueryDto 查询参数
     * @return Result<List < OrdersVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryOrdersList")
    public Result<List<OrdersVO>> queryOrdersList(@RequestBody OrdersQueryDto ordersQueryDto) {
        return ordersService.queryOrdersList(ordersQueryDto);
    }

    /**
     * 订单确定退款
     *
     * @param ordersId 订单ID
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/refund/{ordersId}")
    public Result<String> refund(@PathVariable Integer ordersId) {
        return ordersService.refund(ordersId);
    }

    /**
     * 查询
     *
     * @param ordersQueryDto 查询参数
     * @return Result<List < OrdersVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<OrdersVO>> query(@RequestBody OrdersQueryDto ordersQueryDto) {
        return ordersService.query(ordersQueryDto);
    }
}
