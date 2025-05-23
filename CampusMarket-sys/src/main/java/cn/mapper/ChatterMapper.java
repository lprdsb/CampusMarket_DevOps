package cn.mapper;

import cn.pojo.dto.query.extend.ChatterQueryDto;
import cn.pojo.entity.Chatter;
import cn.pojo.vo.ChatterVO;

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
