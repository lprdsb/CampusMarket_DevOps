package code_sys.LayerService.Impl;

import code_sys.LayerMap.LayerOperationLogMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.OperationLogQueryDto;
import code_sys.Po.Entity.OperationLog;
import code_sys.Po.Vo.OperationLogVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LayerOperationLogServiceImplTest {

    @Mock
    private LayerOperationLogMapper layerOperationLogMapper;

    @Mock
    private Logger logger;

    @InjectMocks
    private LayerOperationLogServiceImpl layerOperationLogService;

    private OperationLog mockOperationLog;
    private OperationLogQueryDto mockQueryDto;
    private OperationLogVO mockOperationLogVO;
    private List<Integer> mockIds;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        mockOperationLog = new OperationLog();
        mockOperationLog.setUserId(1);
        mockOperationLog.setDetail("测试操作详情");

        mockQueryDto = new OperationLogQueryDto();
        mockOperationLogVO = new OperationLogVO();
        mockOperationLogVO.setId(1);
        mockOperationLogVO.setDetail("测试操作详情");

        mockIds = Arrays.asList(1, 2, 3);
    }

    // ========== save 方法测试 ==========

    @Test
    void save_Success() {
        // 模拟保存成功，返回1表示影响1行
        when(layerOperationLogMapper.save(any(OperationLog.class))).thenReturn(1);

        // 执行测试
        Result<String> result = layerOperationLogService.save(mockOperationLog);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证Mapper方法被调用
        verify(layerOperationLogMapper, times(1)).save(mockOperationLog);
    }

    @Test
    void save_NullOperationLog() {
        // 执行测试：传入null
        Result<String> result = layerOperationLogService.save(null);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("日志参数不能为空", result.getMsg());

        // 验证Mapper方法未被调用
        verify(layerOperationLogMapper, never()).save(any(OperationLog.class));
    }

    @Test
    void save_NullUserId() {
        // 准备测试数据：用户ID为null
        mockOperationLog.setUserId(null);

        // 执行测试
        Result<String> result = layerOperationLogService.save(mockOperationLog);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("用户ID不能为空", result.getMsg());

        // 验证Mapper方法未被调用
        verify(layerOperationLogMapper, never()).save(any(OperationLog.class));
    }

    @Test
    void save_EmptyDetail() {
        // 准备测试数据：详情为空
        mockOperationLog.setDetail("");

        // 执行测试
        Result<String> result = layerOperationLogService.save(mockOperationLog);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("操作详情不能为空", result.getMsg());

        // 验证Mapper方法未被调用
        verify(layerOperationLogMapper, never()).save(any(OperationLog.class));
    }
    // ========== batchDelete 方法测试 ==========

    @Test
    void batchDelete_Success() {
        // 模拟Mapper成功删除
        doNothing().when(layerOperationLogMapper).batchDelete(anyList());

        // 执行测试
        Result<String> result = layerOperationLogService.batchDelete(mockIds);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证Mapper方法被调用
        verify(layerOperationLogMapper, times(1)).batchDelete(mockIds);
    }

    @Test
    void batchDelete_EmptyList() {
        // 执行测试：传入空列表
        Result<String> result = layerOperationLogService.batchDelete(Collections.emptyList());

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("删除列表不能为空", result.getMsg());

        // 验证Mapper方法未被调用
        verify(layerOperationLogMapper, never()).batchDelete(anyList());
    }

    @Test
    void batchDelete_NullList() {
        // 执行测试：传入null
        Result<String> result = layerOperationLogService.batchDelete(null);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("删除列表不能为空", result.getMsg());

        // 验证Mapper方法未被调用
        verify(layerOperationLogMapper, never()).batchDelete(anyList());
    }
    // ========== query 方法测试 ==========

    @Test
    void query_Success() {
        // 模拟Mapper返回数据
        List<OperationLogVO> mockList = Arrays.asList(mockOperationLogVO);
        when(layerOperationLogMapper.queryCount(any(OperationLogQueryDto.class))).thenReturn(1);
        when(layerOperationLogMapper.query(any(OperationLogQueryDto.class))).thenReturn(mockList);

        // 执行测试
        Result<List<OperationLogVO>> result = layerOperationLogService.query(mockQueryDto);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证Mapper方法被调用
        verify(layerOperationLogMapper, times(1)).queryCount(mockQueryDto);
        verify(layerOperationLogMapper, times(1)).query(mockQueryDto);
    }

    @Test
    void query_EmptyResult() {
        // 模拟Mapper返回空结果
        when(layerOperationLogMapper.queryCount(any(OperationLogQueryDto.class))).thenReturn(0);
        when(layerOperationLogMapper.query(any(OperationLogQueryDto.class))).thenReturn(Collections.emptyList());

        // 执行测试
        Result<List<OperationLogVO>> result = layerOperationLogService.query(mockQueryDto);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证Mapper方法被调用
        verify(layerOperationLogMapper, times(1)).queryCount(mockQueryDto);
        verify(layerOperationLogMapper, times(1)).query(mockQueryDto);
    }

    @Test
    void query_MapperThrowsException() {
        // 模拟Mapper抛出异常
        when(layerOperationLogMapper.queryCount(any(OperationLogQueryDto.class)))
                .thenThrow(new RuntimeException("数据库连接失败"));

        // 执行测试
        Result<List<OperationLogVO>> result = layerOperationLogService.query(mockQueryDto);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("操作日志删除失败", result.getMsg());

        // 验证Mapper方法被调用
        verify(layerOperationLogMapper, times(1)).queryCount(mockQueryDto);
        // 注意：query方法不会被调用，因为queryCount已经抛出异常
        verify(layerOperationLogMapper, never()).query(any(OperationLogQueryDto.class));
    }

    @Test
    void query_CountZero() {
        // 模拟Mapper返回计数为0
        when(layerOperationLogMapper.queryCount(any(OperationLogQueryDto.class))).thenReturn(0);
        when(layerOperationLogMapper.query(any(OperationLogQueryDto.class))).thenReturn(Collections.emptyList());

        // 执行测试
        Result<List<OperationLogVO>> result = layerOperationLogService.query(mockQueryDto);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证Mapper方法被调用
        verify(layerOperationLogMapper, times(1)).queryCount(mockQueryDto);
        verify(layerOperationLogMapper, times(1)).query(mockQueryDto);
    }
}