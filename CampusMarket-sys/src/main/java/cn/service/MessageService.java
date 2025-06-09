package cn.service;

import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.CategoryQueryDto;
import cn.pojo.dto.query.extend.MessageQueryDto;
import cn.pojo.entity.Category;
import cn.pojo.entity.Message;
import cn.pojo.vo.MessageVO;

import java.util.List;


public interface MessageService {

    Result<String> save(Message message);

    Result<String> setRead(Integer userId);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<MessageVO>> query(MessageQueryDto messageQueryDto);

}
