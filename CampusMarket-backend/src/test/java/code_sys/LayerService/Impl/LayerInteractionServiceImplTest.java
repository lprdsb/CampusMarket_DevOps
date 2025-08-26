package code_sys.LayerService.Impl;

import code_sys.LayerService.Impl.LayerInteractionServiceImpl;
import code_sys.LayerMap.LayerInteractionMapper;
import code_sys.LayerMap.LayerMessageMapper;
import code_sys.LayerMap.LayerProductMapper;
import code_sys.LayerMap.LayerUserMapper;
import code_sys.LayerService.StarService;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.InteractionQueryDto;
import code_sys.Po.Dto.query.sons.ProductQueryDto;
import code_sys.Po.Dto.query.sons.StarQueryDto;
import code_sys.Po.Em.InteractionType;
import code_sys.Po.Entity.Interaction;
import code_sys.Po.Entity.Message;
import code_sys.Po.Entity.User;
import code_sys.Po.Vo.ProductVO;
import code_sys.Po.Vo.StarVo;
import code_sys.Interceptor.LocalThreadHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LayerInteractionServiceImplTest {

    @Mock
    private LayerInteractionMapper layerInteractionMapper;

    @Mock
    private LayerProductMapper layerProductMapper;

    @Mock
    private LayerMessageMapper layerMessageMapper;

    @Mock
    private LayerUserMapper layerUserMapper;

    @Mock
    private StarService starService;

    @InjectMocks
    private LayerInteractionServiceImpl interactionService;

    @Before
    public void setUp() {

    }

    @Test
    public void testLikeProduct_Success() {
        // 准备测试数据
        ProductVO product = new ProductVO();
        product.setId(1);
        product.setUserId(2); // 确保不是当前用户自己的商品
        product.setName("测试商品");

        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setUserName("测试用户");

        // 模拟行为
        when(layerProductMapper.query(any(ProductQueryDto.class)))
                .thenReturn(Collections.singletonList(product));
        when(layerUserMapper.getByActive(any(User.class)))
                .thenReturn(currentUser);
        when(layerInteractionMapper.queryCount(any(InteractionQueryDto.class)))
                .thenReturn(0);

        // 执行测试
        Result<String> result = interactionService.likeProduct(1);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("卖家已感受到你的热情，快下单吧!", result.getMsg());
        verify(layerInteractionMapper, times(1)).save(any(Interaction.class));
        verify(layerMessageMapper, times(1)).save(any(Message.class));
    }

    @Test
    public void testLikeProduct_ProductNotFound() {
        // 模拟行为
        when(layerProductMapper.query(any(ProductQueryDto.class)))
                .thenReturn(Collections.emptyList());

        // 执行测试
        Result<String> result = interactionService.likeProduct(1);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("商品信息查询异常", result.getMsg());
    }

    @Test
    public void testLikeProduct_RepeatOperation() {
        // 准备测试数据
        ProductVO product = new ProductVO();
        product.setId(1);
        product.setUserId(2);

        // 模拟行为
        when(layerProductMapper.query(any(ProductQueryDto.class)))
                .thenReturn(Collections.singletonList(product));
        when(layerInteractionMapper.queryCount(any(InteractionQueryDto.class)))
                .thenReturn(1); // 表示已存在

        // 执行测试
        Result<String> result = interactionService.likeProduct(1);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("请勿重复操作", result.getMsg());
    }

    @Test
    public void testNewProduct_NoFollowers() {
        // 准备测试数据
        ProductVO product = new ProductVO();
        product.setId(1);
        product.setUserId(1);

        // 模拟行为
        when(layerProductMapper.query(any(ProductQueryDto.class)))
                .thenReturn(Collections.singletonList(product));
        when(starService.getStarVos(any(StarQueryDto.class)))
                .thenReturn(Collections.emptyList());

        // 执行测试
        Result<String> result = interactionService.newProduct(1);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerMessageMapper, never()).save(any(Message.class));
    }

    @Test
    public void testSave_Success() {
        // 准备测试数据
        Interaction interaction = new Interaction();
        interaction.setId(1);

        // 执行测试
        Result<String> result = interactionService.save(interaction);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerInteractionMapper, times(1)).save(interaction);
    }

    @Test
    public void testBatchDelete_Success() {
        // 准备测试数据
        List<Integer> ids = Arrays.asList(1, 2, 3);

        // 执行测试
        Result<String> result = interactionService.batchDelete(ids);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerInteractionMapper, times(1)).batchDelete(ids);
    }


    @Test
    public void testView_FirstTime() {
        // 模拟行为
        when(layerInteractionMapper.queryCount(any(InteractionQueryDto.class)))
                .thenReturn(0);

        // 执行测试
        Result<Void> result = interactionService.view(1);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerInteractionMapper, times(1)).save(any(Interaction.class));
    }

    @Test
    public void testView_Repeat() {
        // 模拟行为
        when(layerInteractionMapper.queryCount(any(InteractionQueryDto.class)))
                .thenReturn(1);

        // 执行测试
        Result<Void> result = interactionService.view(1);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerInteractionMapper, never()).save(any(Interaction.class));
    }



    @Test
    public void testBatchDeleteInteraction_WithData() {
        // 准备测试数据
        List<Integer> ids = Arrays.asList(1, 2);

        // 模拟行为
        when(layerInteractionMapper.query(any(InteractionQueryDto.class)))
                .thenReturn(Arrays.asList(
                        createInteraction(1, InteractionType.VIEW),
                        createInteraction(2, InteractionType.VIEW)
                ));

        // 执行测试
        Result<String> result = interactionService.batchDeleteInteraction();

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerInteractionMapper, times(1)).batchDelete(ids);
    }

    @Test
    public void testLikeResult() {
        String result = interactionService.likeResult("用户A", "商品1");
        assertEquals("用户A喜欢商品:商品1", result);
    }

    @Test
    public void testSaveResult() {
        String result = interactionService.saveResult("用户A", "商品1");
        assertEquals("用户A对商品1的交互成功", result);
    }

    @Test
    public void testQueryResult() {
        String result = interactionService.queryResult("商品1");
        assertEquals("成功查找到:商品1", result);
    }

    // 辅助方法
    private Interaction createInteraction(int productId, InteractionType type) {
        Interaction interaction = new Interaction();
        interaction.setId(productId);
        interaction.setProductId(productId);
        interaction.setType(type.getType());
        return interaction;
    }
}