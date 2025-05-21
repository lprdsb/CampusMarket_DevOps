package cn.service;

import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.ChatterQueryDto;
import cn.pojo.dto.query.extend.MessageQueryDto;
import cn.pojo.entity.Chatter;
import cn.pojo.vo.ChatterVO;
import cn.pojo.vo.MessageVO;

import java.util.List;

/**
 * 聊天信息业务逻辑接口
 */
public interface ChatterService {
    Result<String> save(Chatter chatter);

    Result<List<ChatterVO>> query(ChatterQueryDto chatterQueryDto);
}
