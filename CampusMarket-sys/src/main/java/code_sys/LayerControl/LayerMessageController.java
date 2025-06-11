package code_sys.LayerControl;

import code_sys.Aop.Pager;
import code_sys.Interceptor.LocalThreadHolder;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.MessageQueryDto;
import code_sys.Po.Vo.MessageVO;
import code_sys.LayerService.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/message")
public class LayerMessageController {

    @Resource
    private MessageService messageService;


    @PostMapping("/batchDelete")
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        System.out.println("进入接口");
        System.out.println("进入返回函数");
        return messageService.batchDelete(ids);
    }


    @PostMapping("/setRead")
    public Result<String> setRead() {
        System.out.println("进入接口");
        System.out.println("进入返回函数");
        return messageService.setRead(LocalThreadHolder.getUserId());
    }


    @Pager
    @PostMapping("/query")
    public Result<List<MessageVO>> query(@RequestBody MessageQueryDto messageQueryDto) {
        System.out.println("进入接口");
        System.out.println("进入返回函数");
        return messageService.query(messageQueryDto);
    }


    @PostMapping("/queryUser")
    public Result<List<MessageVO>> queryUser(@RequestBody MessageQueryDto messageQueryDto) {
        System.out.println("进入接口");
        messageQueryDto.setUserId(LocalThreadHolder.getUserId());
        System.out.println("进入返回函数");
        return messageService.query(messageQueryDto);
    }

}
