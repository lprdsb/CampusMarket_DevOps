package Cn.Service.Impl;

import Cn.Mapper.MessageMapper;
import Cn.Poto.Api.ApiResult;
import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.MessageQueryDto;
import Cn.Poto.Entity.Message;
import Cn.Poto.Vo.MessageVO;
import Cn.Service.MessageService;
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