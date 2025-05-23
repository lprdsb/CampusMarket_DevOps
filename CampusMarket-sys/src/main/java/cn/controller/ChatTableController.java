package cn.controller;

import cn.context.LocalThreadHolder;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.ChatterQueryDto;
import cn.pojo.vo.ChatterVO;
import cn.service.ChatterService;
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
