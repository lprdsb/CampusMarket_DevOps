package code_sys.LayerService.Impl;

import code_sys.LayerService.Impl.LayerEvaluationsServiceImpl;
import code_sys.LayerMap.LayerEvaluationsMapper;
import code_sys.LayerMap.LayerUserMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.EvaluationsQueryDto;
import code_sys.Po.Entity.Evaluations;
import code_sys.Po.Entity.User;
import code_sys.Po.Vo.CommentParentVO;
import code_sys.Po.Vo.EvaluationsVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LayerEvaluationsServiceImplTest {

    @Mock
    private LayerEvaluationsMapper layerEvaluationsMapper;

    @Mock
    private LayerUserMapper layerUserMapper;

    @InjectMocks
    private LayerEvaluationsServiceImpl evaluationsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert_Success() {
        // 准备测试数据
        Evaluations evaluation = new Evaluations();
        evaluation.setContentId(1);
        evaluation.setContentType("article");

        User user = new User();
        user.setIsWord(false);

        // 模拟行为
        when(layerUserMapper.getByActive(any(User.class))).thenReturn(user);

        // 执行测试
        Result<Object> result = evaluationsService.insert(evaluation);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("评论成功", result.getMsg());
        verify(layerEvaluationsMapper, times(1)).save(evaluation);
        assertNotNull(evaluation.getCreateTime());
    }

    @Test
    public void testInsert_UserBanned() {
        // 准备测试数据
        Evaluations evaluation = new Evaluations();

        User user = new User();
        user.setIsWord(true);

        // 模拟行为
        when(layerUserMapper.getByActive(any(User.class))).thenReturn(user);

        // 执行测试
        Result<Object> result = evaluationsService.insert(evaluation);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("账户已被禁言", result.getMsg());
        verify(layerEvaluationsMapper, never()).save(any());
    }




    @Test
    public void testBatchDelete() {
        // 准备测试数据
        List<Integer> ids = Arrays.asList(1, 2, 3);

        // 执行测试
        Result<Object> result = evaluationsService.batchDelete(ids);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerEvaluationsMapper, times(1)).batchDelete(ids);
    }

    @Test
    public void testDelete() {
        // 执行测试
        Result<String> result = evaluationsService.delete(1);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerEvaluationsMapper, times(1)).batchDelete(anyList());
    }

    @Test
    public void testUpdate() {
        // 准备测试数据
        Evaluations evaluation = new Evaluations();
        evaluation.setId(1);

        // 执行测试
        Result<Void> result = evaluationsService.update(evaluation);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(layerEvaluationsMapper, times(1)).update(evaluation);
    }





    @Test
    public void testShow_IsWord() {
        String result = evaluationsService.show_IsWord("用户");
        assertEquals("用户已被禁言", result);
    }

    @Test
    public void testShowCommentCount() {
        int result = evaluationsService.showCommentCount(5);
        assertEquals(5, result);
    }

    @Test
    public void testAddCountVotes() {
        int result = evaluationsService.addCountVotes(3, 5);
        assertEquals(8, result);
    }

    @Test
    public void testDeleteResult() {
        String result = evaluationsService.deleteResult("admin", "123");
        assertEquals("admin删除了评论：123", result);
    }

    @Test
    public void testNoice() {
        String result = evaluationsService.noice("张三", "李四");
        assertEquals("已通知张三来自李四的点赞", result);
    }
}