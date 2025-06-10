package Cn.Controller;

import Cn.Context.LocalThreadHolder;
import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.ChatterQueryDto;
import Cn.Poto.Entity.Chatter;
import Cn.Poto.Vo.ChatterVO;
import Cn.Poto.Vo.UserVO;
import Cn.Service.ChatterService;
import Cn.Service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatterController {
    @Resource
    private ChatterService chatterService;

    @Resource
    private UserService userService;
    /**
     * 发送信息的保存
     * @param chatter 聊天信息
     * @return 保存成功与否
     */
    @PostMapping(value = "/send")
    @ResponseBody
    public Result<String> send(@RequestBody Chatter chatter) {
        return chatterService.save(chatter);
    }

    /**
     * 查询与这个用户的聊天信息
     * @param chatterQueryDto 聊天传递的信息
     * @return 双向的聊天记录
     */
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<ChatterVO>> query(@RequestBody ChatterQueryDto chatterQueryDto){
        chatterQueryDto.setSenderId(LocalThreadHolder.getUserId());
        return chatterService.query(chatterQueryDto);
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return Result<UserVO>
     */
    @GetMapping(value = "/getById/{id}")
    @ResponseBody
    public Result<UserVO> getById(@PathVariable Integer id) {
        return userService.getById(id);
    }
}
