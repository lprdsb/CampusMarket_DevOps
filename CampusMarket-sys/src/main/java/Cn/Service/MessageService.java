package Cn.Service;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.MessageQueryDto;
import Cn.Poto.Entity.Message;
import Cn.Poto.Vo.MessageVO;

import java.util.List;


public interface MessageService {

    Result<String> save(Message message);

    Result<String> setRead(Integer userId);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<MessageVO>> query(MessageQueryDto messageQueryDto);

}
