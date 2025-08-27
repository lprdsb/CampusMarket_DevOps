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

    public int addd(int x,int y){
        return x+y;
    }

    @Override
    public Result<String> save(Message message) {
        System.out.println("开始进入mapper函数");
        layerMessageMapper.save(message);
        System.out.println("准备返回");
        return ApiResult.success(MSG_SAVED);
    }

    @Override
    public Result<String> setRead(Integer userId) {
        System.out.println("开始进入mapper函数");
        layerMessageMapper.setRead(userId);
        System.out.println("准备返回");
        return ApiResult.success(MSG_READ);
    }

    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        System.out.println("开始进入mapper函数");
        layerMessageMapper.batchDelete(ids);
        System.out.println("准备返回");
        return ApiResult.success(MSG_DELETED);
    }

    @Override
    public Result<List<MessageVO>> query(MessageQueryDto queryDto) {
        System.out.println("开始进入mapper函数");
        int total = layerMessageMapper.queryCount(queryDto);
        List<MessageVO> messages = layerMessageMapper.query(queryDto);
        System.out.println("准备返回");
        return ApiResult.success(messages, total);
    }
}