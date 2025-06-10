package Cn.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import Cn.Poto.Dto.query.extend.StarQueryDto;
import Cn.Poto.Vo.StarVo;

public interface StarMapper {
    List<StarVo> query(StarQueryDto starQueryDto);

    int save(StarQueryDto starQueryDto);

    void batchDelete(@Param(value = "ids") List<Integer> ids);
}
