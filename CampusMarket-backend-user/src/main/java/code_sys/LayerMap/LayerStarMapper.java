package code_sys.LayerMap;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import code_sys.Po.Dto.query.sons.StarQueryDto;
import code_sys.Po.Vo.StarVo;

public interface LayerStarMapper {
    List<StarVo> query(StarQueryDto starQueryDto);

    int save(StarQueryDto starQueryDto);

    void batchDelete(@Param(value = "ids") List<Integer> ids);
}
