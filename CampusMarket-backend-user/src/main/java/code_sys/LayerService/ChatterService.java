package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.ChatterQueryDto;
import code_sys.Po.Entity.Chatter;
import code_sys.Po.Vo.ChatterVO;

import java.util.List;

/**
 * 聊天信息业务逻辑接口
 */
public interface ChatterService {
    Result<String> save(Chatter chatter);

    Result<List<ChatterVO>> query(ChatterQueryDto chatterQueryDto);

    Result<List<ChatterVO>> queryTable(ChatterQueryDto chatterQueryDto);
}
