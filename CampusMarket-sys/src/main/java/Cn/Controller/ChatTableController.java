package Cn.Controller;

import Cn.Context.LocalThreadHolder;
import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.ChatterQueryDto;
import Cn.Poto.Vo.ChatterVO;
import Cn.Service.ChatterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chatTable")
public class ChatTableController {

    @Resource
    private ChatterService chatterService;

    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<ChatterVO>> query(@RequestBody ChatterQueryDto chatterQueryDto){
        chatterQueryDto.setSenderId(LocalThreadHolder.getUserId());
        return chatterService.queryTable(chatterQueryDto);
    }

    @PostMapping(value = "/queryCurrentUser")
    @ResponseBody
    public Integer queryCurrentUser(){
        return LocalThreadHolder.getUserId();
    }
}
