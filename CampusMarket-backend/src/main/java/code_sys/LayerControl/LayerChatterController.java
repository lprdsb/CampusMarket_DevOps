package code_sys.LayerControl;

import code_sys.Interceptor.LocalThreadHolder;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.ChatterQueryDto;
import code_sys.Po.Entity.Chatter;
import code_sys.Po.Vo.ChatterVO;
import code_sys.Po.Vo.UserVO;
import code_sys.LayerService.ChatterService;
import code_sys.LayerService.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class LayerChatterController {
    @Resource
    private ChatterService chatterService;

    @Resource
    private UserService userService;
    /**
     * 发送信息的保存
     * @param chatter 聊天信息
     * @return 保存成功与否
     */
    @PostMapping("/send")
    public Result<String> send(@RequestBody Chatter chatter) {
        return chatterService.save(chatter);
    }

    /**
     * 查询与这个用户的聊天信息
     * @param chatterQueryDto 聊天传递的信息
     * @return 双向的聊天记录
     */
    @PostMapping("/queryUser")
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
    @GetMapping("/getById/{id}")
    public Result<UserVO> getById(@PathVariable Integer id) {
        return userService.getById(id);
    }
}
