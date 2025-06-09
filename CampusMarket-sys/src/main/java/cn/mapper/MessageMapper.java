package cn.mapper;

import cn.pojo.dto.query.extend.MessageQueryDto;
import cn.pojo.entity.Message;
import cn.pojo.vo.MessageVO;
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
