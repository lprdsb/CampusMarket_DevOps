package Cn.Mapper;

import Cn.Poto.Dto.query.extend.ChatterQueryDto;
import Cn.Poto.Entity.Chatter;
import Cn.Poto.Vo.ChatterVO;

import java.util.List;

/**
 * 聊天信息持久化
 */
public interface ChatterMapper {
    /**
     * 新增信息
     *
     * @param chatter
     * @return 行数
     */
    int save(Chatter chatter);

    List<ChatterVO> query(ChatterQueryDto chatterQueryDto);

    List<ChatterVO> queryTable(ChatterQueryDto chatterQueryDto);
}
