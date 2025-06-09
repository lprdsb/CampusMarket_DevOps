package cn.controller;

import cn.aop.Pager;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.InteractionQueryDto;
import cn.pojo.entity.Interaction;
import cn.pojo.vo.ProductVO;
import cn.service.InteractionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/interaction")
public class InteractionController {

    @Resource
    private InteractionService interactionService;

    @PostMapping(value = "/likeProduct/{productId}")
    @ResponseBody
    public Result<String> likeProduct(@PathVariable Integer productId) {
        return interactionService.likeProduct(productId);
    }


    @PostMapping(value = "/saveOperation/{productId}")
    @ResponseBody
    public Result<Boolean> saveOperation(@PathVariable Integer productId) {
        return interactionService.saveOperation(productId);
    }


    @PostMapping(value = "/batchDeleteView")
    @ResponseBody
    public Result<String> batchDeleteInteraction() {
        return interactionService.batchDeleteInteraction();
    }


    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return interactionService.batchDelete(ids);
    }


    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<Interaction>> query(@RequestBody InteractionQueryDto interactionQueryDto) {
        return interactionService.query(interactionQueryDto);
    }

    @PostMapping(value = "/view/{productId}")
    @ResponseBody
    public Result<Void> view(@PathVariable Integer productId) {
        return interactionService.view(productId);
    }

    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<ProductVO>> queryUser() {
        return interactionService.queryUser();
    }


    @PostMapping(value = "/myView")
    @ResponseBody
    public Result<List<ProductVO>> myView() {
        return interactionService.myView();
    }

}
