package Cn.Controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import Cn.Service.StarService;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.StarQueryDto;
import Cn.Poto.Vo.StarVo;
import Cn.Poto.Vo.UserVO;

@RestController
@RequestMapping("/star")
public class StarController {

    @Resource
    private StarService starService;

    /**
     * 查询用户自己关注的人
     *
     * @return Result<List < UserVO>> 响应结果
     */
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<StarVo>> query(@RequestBody StarQueryDto starQueryDto) {
        // System.out.println(starQueryDto);
        return starService.query(starQueryDto);
    }

    @PostMapping(value = "/queryByUser1/{userId}")
    @ResponseBody
    public Result<List<UserVO>> queryByUser1(@PathVariable Integer userId) {
        // System.out.println(userId);
        return starService.queryByUser1(userId);
    }

    @PostMapping(value = "/queryByUser2/{userId}")
    @ResponseBody
    public Result<List<UserVO>> queryByUser2(@PathVariable Integer userId) {
        return starService.queryByUser2(userId);
    }

    @PostMapping(value = "/starOperation/{userId}")
    @ResponseBody
    public Result<Boolean> starOperation(@PathVariable Integer userId) {
        return starService.starOperation(userId);
    }
}
