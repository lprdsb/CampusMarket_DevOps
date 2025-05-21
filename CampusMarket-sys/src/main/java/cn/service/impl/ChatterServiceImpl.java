package cn.service.impl;

import cn.mapper.ChatterMapper;
import cn.pojo.api.ApiResult;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.ChatterQueryDto;
import cn.pojo.dto.query.extend.MessageQueryDto;
import cn.pojo.entity.Chatter;
import cn.pojo.vo.ChatterVO;
import cn.pojo.vo.MessageVO;
import cn.service.ChatterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChatterServiceImpl implements ChatterService {
    @Resource
    private ChatterMapper chatterMapper;

    @Override
    public Result<String> save(Chatter chatter){
        chatterMapper.save(chatter);
        return ApiResult.success("聊天信息发送成功");
    }

    @Override
    public Result<List<ChatterVO>> query(ChatterQueryDto chatterQueryDto){
        chatterMapper.query(chatterQueryDto);
        List<ChatterVO>  chatterVOList = chatterMapper.query(chatterQueryDto);
        return ApiResult.success(chatterVOList);
    }
}
