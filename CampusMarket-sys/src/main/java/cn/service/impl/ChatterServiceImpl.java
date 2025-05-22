package cn.service.impl;

import cn.context.LocalThreadHolder;
import cn.mapper.ChatterMapper;
import cn.pojo.api.ApiResult;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.ChatterQueryDto;
import cn.pojo.entity.Chatter;
import cn.pojo.vo.ChatterVO;
import cn.service.ChatterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatterServiceImpl implements ChatterService {
    @Resource
    private ChatterMapper chatterMapper;

    @Override
    public Result<String> save(Chatter chatter){
        chatter.setCreateTime(LocalDateTime.now());
        chatter.setSenderId(LocalThreadHolder.getUserId());
        chatterMapper.save(chatter);
        return ApiResult.success("聊天信息发送成功");
    }

    @Override
    public Result<List<ChatterVO>> query(ChatterQueryDto chatterQueryDto){
        // 第一次查询：原始参数
        System.out.println(chatterQueryDto);
        List<ChatterVO> firstList = chatterMapper.query(chatterQueryDto);

        // 保存原始参数，避免后续污染
        Integer originalSenderId = chatterQueryDto.getSenderId();
        Integer originalReceiverId = chatterQueryDto.getReceiverId();

        // 交换 senderId 和 receiverId
        chatterQueryDto.setSenderId(originalReceiverId);
        chatterQueryDto.setReceiverId(originalSenderId);

        // 第二次查询：交换后的参数
        List<ChatterVO> secondList = chatterMapper.query(chatterQueryDto);

        // 合并两次结果
        List<ChatterVO> combinedList = new ArrayList<>(firstList);
        combinedList.addAll(secondList);

        // 恢复原始参数
        chatterQueryDto.setSenderId(originalSenderId);
        chatterQueryDto.setReceiverId(originalReceiverId);
        System.out.println(combinedList);
        return ApiResult.success(combinedList);
    }
}
