package cn.service.impl;

import cn.mapper.MessageMapper;
import cn.pojo.api.ApiResult;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.MessageQueryDto;
import cn.pojo.entity.Message;
import cn.pojo.vo.MessageVO;
import cn.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息服务实现类
 * 重构说明：
 * 1. 使用构造器注入替代字段注入
 * 2. 优化方法命名和注释
 * 3. 提取重复的成功消息为常量
 * 4. 增强查询方法的可读性
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String MSG_SAVED = "消息新增成功";
    private static final String MSG_READ = "消息已读";
    private static final String MSG_DELETED = "消息删除成功";

    private final MessageMapper messageMapper;

    /**
     * 创建新消息
     * @param message 消息实体
     * @return 操作结果
     */
    @Override
    public Result<String> save(Message message) {
        messageMapper.save(message);
        return ApiResult.success(MSG_SAVED);
    }

    /**
     * 标记用户所有消息为已读状态
     * @param userId 用户ID
     * @return 操作结果
     */
    @Override
    public Result<String> setRead(Integer userId) {
        messageMapper.setRead(userId);
        return ApiResult.success(MSG_READ);
    }

    /**
     * 批量删除消息
     * @param ids 消息ID集合
     * @return 操作结果
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        messageMapper.batchDelete(ids);
        return ApiResult.success(MSG_DELETED);
    }

    /**
     * 分页查询消息
     * @param queryDto 查询条件
     * @return 分页结果(包含消息列表和总数)
     */
    @Override
    public Result<List<MessageVO>> query(MessageQueryDto queryDto) {
        int total = messageMapper.queryCount(queryDto);
        List<MessageVO> messages = messageMapper.query(queryDto);
        return ApiResult.success(messages, total);
    }
}