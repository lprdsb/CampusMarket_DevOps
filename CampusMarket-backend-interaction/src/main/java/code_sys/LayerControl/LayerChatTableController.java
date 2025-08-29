package code_sys.LayerControl;

import code_sys.Interceptor.LocalThreadHolder;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.ChatterQueryDto;
import code_sys.Po.Vo.ChatterVO;
import code_sys.LayerService.ChatterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/chatTable")
public class LayerChatTableController {

    @Resource
    private ChatterService chatterService;

    @PostMapping("/queryUser")
    public Result<List<ChatterVO>> query(@RequestBody ChatterQueryDto chatterQueryDto){
        chatterQueryDto.setSenderId(LocalThreadHolder.getUserId());
        return chatterService.queryTable(chatterQueryDto);
    }

    @PostMapping("/queryCurrentUser")
    public Integer queryCurrentUser(){
        return LocalThreadHolder.getUserId();
    }
}
