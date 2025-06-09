package cn.controller;

import cn.aop.Pager;
import cn.context.LocalThreadHolder;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.MessageQueryDto;
import cn.pojo.vo.MessageVO;
import cn.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;


    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return messageService.batchDelete(ids);
    }


    @PostMapping(value = "/setRead")
    @ResponseBody
    public Result<String> setRead() {
        return messageService.setRead(LocalThreadHolder.getUserId());
    }


    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<MessageVO>> query(@RequestBody MessageQueryDto messageQueryDto) {
        return messageService.query(messageQueryDto);
    }


    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<MessageVO>> queryUser(@RequestBody MessageQueryDto messageQueryDto) {
        messageQueryDto.setUserId(LocalThreadHolder.getUserId());
        return messageService.query(messageQueryDto);
    }

}
