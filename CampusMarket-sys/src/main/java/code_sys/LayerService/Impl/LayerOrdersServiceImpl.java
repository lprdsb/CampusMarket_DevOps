package code_sys.LayerService.Impl;

import code_sys.Aop.Log;
import code_sys.LayerEnvironment.LocalThreadHolder;
import code_sys.LayerMap.LayerOrdersMapper;
import code_sys.LayerMap.LayerProductMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.OrdersQueryDto;
import code_sys.Po.Entity.Orders;
import code_sys.Po.Vo.OrdersVO;
import code_sys.LayerService.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LayerOrdersServiceImpl implements OrdersService {

    private static final Logger logger = LoggerFactory.getLogger(LayerOrdersServiceImpl.class);

    @Resource
    private LayerOrdersMapper layerOrdersMapper;

    @Resource
    private LayerProductMapper layerProductMapper;

    /**
     * 创建新订单（使用@Log注解记录操作日志）
     */
    @Override
    @Log(detail = "创建新订单")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> save(Orders orders) {
        try {
            // 参数验证
            validateOrderCreation(orders);

            // 填充必要字段
            fillCreateOrderFields(orders);

            // 保存订单
            layerOrdersMapper.save(orders);

            return ApiResult.success("订单创建成功");
        } catch (IllegalArgumentException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error("创建订单失败: {}", e.getMessage(), e);
            return ApiResult.error("系统内部错误");
        }
    }

    private void validateOrderCreation(Orders orders) {
        if (orders == null) {
            throw new IllegalArgumentException("订单参数不能为空");
        }
        if (orders.getProductId() == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        if (orders.getBuyNumber() == null || orders.getBuyNumber() <= 0) {
            throw new IllegalArgumentException("购买数量无效");
        }
    }

    private void fillCreateOrderFields(Orders orders) {
        orders.setUserId(LocalThreadHolder.getUserId());
        orders.setTradeStatus(false); // 初始未支付状态
        orders.setCreateTime(LocalDateTime.now());
        orders.setRefundStatus(false); // 初始未退款状态
        orders.setIsRefundConfirm(false);
    }

    /**
     * 更新订单信息
     */
    @Override
    @Log(detail = "更新订单信息")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> update(Orders orders) {
        try {
            // 参数验证
            if (orders == null || orders.getId() == null) {
                return ApiResult.error("订单ID不能为空");
            }

            // 执行更新
            layerOrdersMapper.update(orders);

            return ApiResult.success("订单更新成功");
        } catch (Exception e) {
            logger.error("更新订单失败: ID={}, {}", orders.getId(), e.getMessage(), e);
            return ApiResult.error("更新订单失败");
        }
    }

    private boolean canUserUpdateOrder(Orders order) {
        // 实际业务中检查用户权限
        return true;
    }

    /**
     * 批量删除订单
     */
    @Override
    @Log(detail = "批量删除订单")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> batchDelete(List<Integer> ids) {
        try {
            // 参数验证
            if (CollectionUtils.isEmpty(ids)) {
                return ApiResult.error("删除列表不能为空");
            }

            // 检查操作权限
            if (!hasPermissionToDelete(ids)) {
                return ApiResult.error("无权删除这些订单");
            }

            // 执行删除
            layerOrdersMapper.batchDelete(ids);

            return ApiResult.success("成功删除" + ids.size() + "条订单");
        } catch (Exception e) {
            logger.error("批量删除订单失败: {}", e.getMessage(), e);
            return ApiResult.error("删除订单失败");
        }
    }

    private boolean hasPermissionToDelete(List<Integer> orderIds) {
        // 实际业务中检查用户权限
        return true;
    }

    /**
     * 订单查询（分页）
     */
    @Override
    public Result<List<OrdersVO>> query(OrdersQueryDto ordersQueryDto) {
        try {
            int totalCount = layerOrdersMapper.queryCount(ordersQueryDto);
            List<OrdersVO> ordersVOList = layerOrdersMapper.query(ordersQueryDto);
            return ApiResult.success(ordersVOList, totalCount);
        } catch (Exception e) {
            logger.error("订单查询失败: {}", e.getMessage(), e);
            return ApiResult.error("查询失败");
        }
    }

    /**
     * 查询卖家订单（用户发布的商品产生的订单）
     */
    @Override
    public Result<List<OrdersVO>> queryOrdersList(OrdersQueryDto ordersQueryDto) {
        try {
            // 获取当前用户发布的商品ID
            List<Integer> productIds = layerProductMapper.queryProductIds(LocalThreadHolder.getUserId());

            if (CollectionUtils.isEmpty(productIds)) {
                return ApiResult.success(new ArrayList<>());
            }

            // 设置商品ID查询条件
            ordersQueryDto.setProductIds(productIds);

            // 执行查询
            List<OrdersVO> ordersVOList = layerOrdersMapper.queryByProductIds(ordersQueryDto);

            return ApiResult.success(ordersVOList);
        } catch (Exception e) {
            logger.error("卖家订单查询失败: user={}", LocalThreadHolder.getUserId(), e);
            return ApiResult.error("查询失败");
        }
    }

    /**
     * 处理订单退款（使用@Log注解记录操作日志）
     */
    @Override
    @Log(detail = "处理订单退款")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> refund(Integer ordersId) {
        try {
            // 执行退款操作
            executeRefundOperation(ordersId);

            return ApiResult.success("退款成功");
        } catch (Exception e) {
            logger.error("退款操作失败: orderID={}, {}", ordersId, e.getMessage(), e);
            return ApiResult.error("退款处理失败");
        }
    }

    private boolean canUserRefundOrder(Orders order) {
        // 实际业务中检查用户权限
        return true;
    }

    private void executeRefundOperation(Integer ordersId) {
        Orders orders = new Orders();
        orders.setId(ordersId);
        orders.setRefundStatus(true);
        orders.setIsRefundConfirm(true);
        orders.setRefundTime(LocalDateTime.now());
        layerOrdersMapper.update(orders);
    }
}