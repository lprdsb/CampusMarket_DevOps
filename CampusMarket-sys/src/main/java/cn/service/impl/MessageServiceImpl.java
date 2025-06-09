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


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String MSG_SAVED = "消息新增成功";
    private static final String MSG_READ = "消息已读";
    private static final String MSG_DELETED = "消息删除成功";

    private final MessageMapper messageMapper;

    @Override
    public Result<String> save(Message message) {
        messageMapper.save(message);
        return ApiResult.success(MSG_SAVED);
    }

    @Override
    public Result<String> setRead(Integer userId) {
        messageMapper.setRead(userId);
        return ApiResult.success(MSG_READ);
    }

    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        messageMapper.batchDelete(ids);
        return ApiResult.success(MSG_DELETED);
    }

    @Override
    public Result<List<MessageVO>> query(MessageQueryDto queryDto) {
        int total = messageMapper.queryCount(queryDto);
        List<MessageVO> messages = messageMapper.query(queryDto);
        return ApiResult.success(messages, total);
    }
}