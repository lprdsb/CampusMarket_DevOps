package code_sys.LayerService.Impl;

import code_sys.Aop.Pager;
import code_sys.Interceptor.LocalThreadHolder;
import code_sys.LayerControl.LayerMessageController;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.MessageQueryDto;
import code_sys.Po.Entity.Message;
import code_sys.Po.Vo.MessageVO;
import code_sys.LayerService.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LayerMessageServiceImplTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private LayerMessageController layerMessageController;

    private List<Integer> mockIds;
    private MessageQueryDto mockQueryDto;
    private List<MessageVO> mockMessageList;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        mockIds = Arrays.asList(1, 2, 3);

        mockQueryDto = new MessageQueryDto();
        mockQueryDto.setUserId(1);

        MessageVO message1 = new MessageVO();
        message1.setId(1);
        message1.setContent("Test message 1");

        MessageVO message2 = new MessageVO();
        message2.setId(2);
        message2.setContent("Test message 2");

        mockMessageList = Arrays.asList(message1, message2);

    }

    @Test
    void save_Success() {
        // 正确模拟有返回值的方法
        Result<String> successResult = Result.success("消息新增成功");
        when(messageService.save(any(Message.class))).thenReturn(successResult);

        // 执行测试
        Result<String> result = messageService.save(mockMessageList.get(0));

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证方法被调用
        verify(messageService, times(1)).save(mockMessageList.get(0));
    }

    @Test
    void save_Failure() {
        // 模拟失败情况
        Result<String> errorResult = Result.error(400, "保存失败");
        when(messageService.save(any(Message.class))).thenReturn(errorResult);

        // 执行测试
        Result<String> result = messageService.save(mockMessageList.get(0));

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("保存失败", result.getMsg());
    }

    // ========== setRead 方法测试 ==========

    @Test
    void setRead_Success() {
        // 模拟成功
        Result<String> successResult = Result.success("标记已读成功");
        when(messageService.setRead(anyInt())).thenReturn(successResult);

        // 执行测试
        Result<String> result = messageService.setRead(1);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void setRead_Failure() {
        // 模拟失败
        Result<String> errorResult = Result.error(400, "标记已读失败");
        when(messageService.setRead(anyInt())).thenReturn(errorResult);

        // 执行测试
        Result<String> result = messageService.setRead(1);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("标记已读失败", result.getMsg());
    }

    @Test
    void batchDelete_Success() {
        // 模拟服务层返回成功结果
        Result<String> mockResult = Result.success("删除成功");
        when(messageService.batchDelete(mockIds)).thenReturn(mockResult);

        // 执行测试
        Result<String> result = layerMessageController.batchDelete(mockIds);

        // 验证结果
        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getCode());

        // 验证服务方法被调用
        verify(messageService, times(1)).batchDelete(mockIds);
    }

    @Test
    void batchDelete_Error() {
        // 模拟服务层返回错误结果
        Result<String> mockResult = Result.error(400, "删除失败");
        when(messageService.batchDelete(mockIds)).thenReturn(mockResult);

        // 执行测试
        Result<String> result = layerMessageController.batchDelete(mockIds);

        // 验证结果
        assertNotNull(result);
        assertEquals("删除失败", result.getMsg());
        assertNotEquals(HttpStatus.OK.value(), result.getCode());

        // 验证服务方法被调用
        verify(messageService, times(1)).batchDelete(mockIds);
    }

    @Test
    void query_Success() {
        // 模拟服务层返回成功结果
        Result<List<MessageVO>> mockResult = Result.success(mockMessageList.toString());
        when(messageService.query(any(MessageQueryDto.class))).thenReturn(mockResult);

        // 执行测试
        Result<List<MessageVO>> result = layerMessageController.query(mockQueryDto);

        // 验证结果
        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getCode());

        // 验证服务方法被调用
        verify(messageService, times(1)).query(mockQueryDto);
    }

    @Test
    void query_Fail() {
        // 模拟服务层返回错误结果
        String errorMessage = "数据库连接失败";
        Result<List<MessageVO>> mockErrorResult = Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);

        // 设置模拟行为：当调用query方法时返回错误
        when(messageService.query(any(MessageQueryDto.class))).thenReturn(mockErrorResult);

        // 执行测试
        Result<List<MessageVO>> result = layerMessageController.query(mockQueryDto);

        // 验证结果：返回错误状态码和消息
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals(errorMessage, result.getMsg());

        // 验证服务方法被调用
        verify(messageService, times(1)).query(mockQueryDto);
    }
}