package code_sys.LayerService.Impl;

import code_sys.LayerEnvironment.LocalThreadHolder;
import code_sys.LayerMap.LayerChatterMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.ChatterQueryDto;
import code_sys.Po.Entity.Chatter;
import code_sys.Po.Vo.ChatterVO;
import code_sys.LayerService.ChatterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LayerChatterServiceImpl implements ChatterService {
    @Resource
    private LayerChatterMapper layerChatterMapper;

    @Override
    public Result<String> save(Chatter chatter){
        chatter.setCreateTime(LocalDateTime.now());
        chatter.setSenderId(LocalThreadHolder.getUserId());
        layerChatterMapper.save(chatter);
        return ApiResult.success("聊天信息发送成功");
    }

    @Override
    public Result<List<ChatterVO>> query(ChatterQueryDto chatterQueryDto){
        // 第一次查询：原始参数
        List<ChatterVO> firstList = layerChatterMapper.query(chatterQueryDto);

        // 保存原始参数，避免后续污染
        Integer originalSenderId = chatterQueryDto.getSenderId();
        Integer originalReceiverId = chatterQueryDto.getReceiverId();

        // 交换 senderId 和 receiverId
        chatterQueryDto.setSenderId(originalReceiverId);
        chatterQueryDto.setReceiverId(originalSenderId);

        // 第二次查询：交换后的参数
        List<ChatterVO> secondList = layerChatterMapper.query(chatterQueryDto);

        // 合并两次结果
        List<ChatterVO> combinedList = new ArrayList<>(firstList);
        combinedList.addAll(secondList);

        // 恢复原始参数
        chatterQueryDto.setSenderId(originalSenderId);
        chatterQueryDto.setReceiverId(originalReceiverId);
        return ApiResult.success(combinedList);
    }

    @Override
    public Result<List<ChatterVO>> queryTable(ChatterQueryDto chatterQueryDto){
        return ApiResult.success(layerChatterMapper.queryTable(chatterQueryDto));
    }
}
