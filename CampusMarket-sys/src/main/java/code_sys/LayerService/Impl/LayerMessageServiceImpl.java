package code_sys.LayerService.Impl;

import code_sys.LayerMap.LayerMessageMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.MessageQueryDto;
import code_sys.Po.Entity.Message;
import code_sys.Po.Vo.MessageVO;
import code_sys.LayerService.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LayerMessageServiceImpl implements MessageService {

    private static final String MSG_SAVED = "消息新增成功";
    private static final String MSG_READ = "消息已读";
    private static final String MSG_DELETED = "消息删除成功";

    private final LayerMessageMapper layerMessageMapper;

    @Override
    public Result<String> save(Message message) {
        layerMessageMapper.save(message);
        return ApiResult.success(MSG_SAVED);
    }

    @Override
    public Result<String> setRead(Integer userId) {
        layerMessageMapper.setRead(userId);
        return ApiResult.success(MSG_READ);
    }

    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        layerMessageMapper.batchDelete(ids);
        return ApiResult.success(MSG_DELETED);
    }

    @Override
    public Result<List<MessageVO>> query(MessageQueryDto queryDto) {
        int total = layerMessageMapper.queryCount(queryDto);
        List<MessageVO> messages = layerMessageMapper.query(queryDto);
        return ApiResult.success(messages, total);
    }
}