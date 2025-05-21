package cn.controller;

import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.ChatterQueryDto;
import cn.pojo.entity.Chatter;
import cn.pojo.vo.ChatterVO;
import cn.pojo.vo.MessageVO;
import cn.service.ChatterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatterController {
    @Resource
    private ChatterService chatterService;

    /**
     * 发送信息的保存
     * @param chatter
     * @return
     */
    @PostMapping(value = "/send")
    @ResponseBody
    public Result<String> send(@RequestBody Chatter chatter) {
        return chatterService.save(chatter);
    }

    /**
     * 查询与这个用户的聊天信息
     * @param chatterQueryDto
     * @return
     */
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<ChatterVO>> query(@RequestBody ChatterQueryDto chatterQueryDto){
        return chatterService.query(chatterQueryDto);
    }


}
