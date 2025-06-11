package code_sys.LayerControl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import code_sys.LayerService.StarService;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.StarQueryDto;
import code_sys.Po.Vo.StarVo;
import code_sys.Po.Vo.UserVO;

@RestController
@RequestMapping("/star")
public class LayerStarController {

    @Resource
    private StarService starService;

    /**
     * 查询用户自己关注的人
     *
     * @return Result<List < UserVO>> 响应结果
     */
    @PostMapping("/query")
    public Result<List<StarVo>> query(@RequestBody StarQueryDto starQueryDto) {
        // System.out.println(starQueryDto);
        return starService.query(starQueryDto);
    }

    @PostMapping("/queryByUser1/{userId}")
    public Result<List<UserVO>> queryByUser1(@PathVariable Integer userId) {
        // System.out.println(userId);
        return starService.queryByUser1(userId);
    }

    @PostMapping("/queryByUser2/{userId}")
    public Result<List<UserVO>> queryByUser2(@PathVariable Integer userId) {
        return starService.queryByUser2(userId);
    }

    @PostMapping("/starOperation/{userId}")
    public Result<Boolean> starOperation(@PathVariable Integer userId) {
        return starService.starOperation(userId);
    }
}
