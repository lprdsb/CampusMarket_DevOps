package Cn.Mapper;

import Cn.Poto.Dto.query.extend.InteractionQueryDto;
import Cn.Poto.Entity.Interaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface InteractionMapper {


    int save(Interaction interaction);


    List<Interaction> query(InteractionQueryDto interactionQueryDto);


    List<Interaction> queryByProductIds(@Param(value = "ids") List<Integer> ids);


    int queryCount(InteractionQueryDto interactionQueryDto);


    void batchDelete(@Param(value = "ids") List<Integer> ids);

    List<Integer> getRecentViews(@Param("userId") Integer userId,@Param("limit") Integer limit);
}
