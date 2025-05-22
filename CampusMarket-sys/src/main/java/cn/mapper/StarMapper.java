package cn.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.pojo.dto.query.extend.StarQueryDto;
import cn.pojo.vo.StarVo;

public interface StarMapper {
    List<StarVo> query(StarQueryDto starQueryDto);

    int save(StarQueryDto starQueryDto);

    void batchDelete(@Param(value = "ids") List<Integer> ids);
}
