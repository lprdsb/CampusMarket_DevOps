package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Dto.query.sons.OrdersQueryDto;
import code_sys.Po.Entity.Orders;
import code_sys.Po.Vo.OrdersVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LayerOrdersServiceImplTest {
    private OrdersService ordersService;

    @BeforeEach
    void setUp() {
        // 可以使用 mock 或 stub，这里仅为示例
        ordersService = new OrdersService() {
            @Override
            public Result<String> save(Orders orders) {
                return ApiResult.success("保存成功", "saved");
            }
            @Override
            public Result<String> update(Orders orders) {
                return ApiResult.success("更新成功", "updated");
            }
            @Override
            public Result<String> batchDelete(List<Integer> ids) {
                return ApiResult.success("删除成功", "deleted");
            }
            @Override
            public Result<List<OrdersVO>> query(OrdersQueryDto ordersQueryDto) {
                return ApiResult.success(Arrays.asList(new OrdersVO()));
            }
            @Override
            public Result<List<OrdersVO>> queryOrdersList(OrdersQueryDto ordersQueryDto) {
                return ApiResult.success(Arrays.asList(new OrdersVO()));
            }
            @Override
            public Result<String> refund(Integer ordersId) {
                return ApiResult.success("退款成功", "refunded");
            }
        };
    }

    @Test
    void testSave() {
        Orders orders = new Orders();
        Result<String> result = ordersService.save(orders);
        assertTrue(result.isSuccess());
        assertEquals("saved", ((ApiResult<String>)result).getData());
    }

    @Test
    void testUpdate() {
        Orders orders = new Orders();
        Result<String> result = ordersService.update(orders);
        assertTrue(result.isSuccess());
        assertEquals("updated", ((ApiResult<String>)result).getData());
    }

    @Test
    void testBatchDelete() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        Result<String> result = ordersService.batchDelete(ids);
        assertTrue(result.isSuccess());
        assertEquals("deleted", ((ApiResult<String>)result).getData());
    }

    @Test
    void testQuery() {
        OrdersQueryDto dto = new OrdersQueryDto();
        Result<List<OrdersVO>> result = ordersService.query(dto);
        assertTrue(result.isSuccess());
        assertNotNull(((ApiResult<List<OrdersVO>>)result).getData());
    }

    @Test
    void testQueryOrdersList() {
        OrdersQueryDto dto = new OrdersQueryDto();
        Result<List<OrdersVO>> result = ordersService.queryOrdersList(dto);
        assertTrue(result.isSuccess());
        assertNotNull(((ApiResult<List<OrdersVO>>)result).getData());
    }

    @Test
    void testRefund() {
        Result<String> result = ordersService.refund(1);
        assertTrue(result.isSuccess());
        assertEquals("refunded", ((ApiResult<String>)result).getData());
    }
}
