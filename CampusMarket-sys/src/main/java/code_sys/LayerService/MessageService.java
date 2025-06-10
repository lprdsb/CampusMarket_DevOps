package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.MessageQueryDto;
import code_sys.Po.Entity.Message;
import code_sys.Po.Vo.MessageVO;

import java.util.List;


public interface MessageService {

    Result<String> save(Message message);

    Result<String> setRead(Integer userId);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<MessageVO>> query(MessageQueryDto messageQueryDto);

}
