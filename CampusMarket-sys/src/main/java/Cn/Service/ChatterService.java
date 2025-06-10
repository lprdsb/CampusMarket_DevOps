package Cn.Service;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.ChatterQueryDto;
import Cn.Poto.Entity.Chatter;
import Cn.Poto.Vo.ChatterVO;

import java.util.List;

/**
 * 聊天信息业务逻辑接口
 */
public interface ChatterService {
    Result<String> save(Chatter chatter);

    Result<List<ChatterVO>> query(ChatterQueryDto chatterQueryDto);

    Result<List<ChatterVO>> queryTable(ChatterQueryDto chatterQueryDto);
}
