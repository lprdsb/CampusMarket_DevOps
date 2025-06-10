package Cn.Mapper;

import Cn.Poto.Dto.query.extend.MessageQueryDto;
import Cn.Poto.Entity.Message;
import Cn.Poto.Vo.MessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息持久化接口
 */
public interface MessageMapper {


    int save(Message message);


    int setRead(@Param(value = "userId") Integer userId);


    List<MessageVO> query(MessageQueryDto messageQueryDto);

    int queryCount(MessageQueryDto messageQueryDto);


    void batchDelete(@Param(value = "ids") List<Integer> ids);

}
