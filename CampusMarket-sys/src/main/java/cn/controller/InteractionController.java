package cn.controller;

import cn.aop.Pager;
import cn.context.LocalThreadHolder;
import cn.mapper.InteractionMapper;
import cn.mapper.MessageMapper;
import cn.mapper.ProductMapper;
import cn.mapper.UserMapper;
import cn.pojo.api.ApiResult;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.InteractionQueryDto;
import cn.pojo.dto.query.extend.ProductQueryDto;
import cn.pojo.dto.query.extend.StarQueryDto;
import cn.pojo.em.InteractionEnum;
import cn.pojo.entity.Interaction;
import cn.pojo.entity.Message;
import cn.pojo.entity.User;
import cn.pojo.vo.ProductVO;
import cn.pojo.vo.StarVo;
import cn.service.InteractionService;
import cn.service.StarService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/interaction")
public class InteractionController {



    private static final String REPEAT_OPERATION_MSG = "请勿重复操作";
    private static final String PRODUCT_NOT_FOUND_MSG = "商品信息查询异常";
    private static final String SELF_OPERATION_MSG = "别自卖自夸!";
    private static final String CANCEL_COLLECT_MSG = "取消收藏成功";

    @Resource
    private InteractionService interactionService;
    @Resource
    private InteractionMapper interactionMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private StarService starService;
    @PostMapping(value = "/likeProduct/{productId}")
    @ResponseBody
    public Result<String> likeProduct(@PathVariable Integer productId) {
        ProductVO product = getProductById(productId);

        if (product == null) {
            return ApiResult.error(PRODUCT_NOT_FOUND_MSG);
        }

        if (isInteractionExist(productId, InteractionEnum.LOVE)) {
            return ApiResult.error(REPEAT_OPERATION_MSG);
        }

        if (isCurrentUserProduct(product)) {
            return ApiResult.error(SELF_OPERATION_MSG);
        }
        return interactionService.likeProduct(productId);
    }


    @PostMapping(value = "/saveOperation/{productId}")
    @ResponseBody
    public Result<Boolean> saveOperation(@PathVariable Integer productId) {
        boolean isCollected = isInteractionExist(productId, InteractionEnum.SAVE);

        if (isCollected) {
            cancelCollection(productId);
            return ApiResult.success(CANCEL_COLLECT_MSG, false);
        }
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
        List<Integer> productIds = getInteractionProductIds(InteractionEnum.SAVE);
        if (productIds.isEmpty()) {
            return ApiResult.success(Collections.emptyList());
        }
        return interactionService.queryUser();
    }


    @PostMapping(value = "/myView")
    @ResponseBody
    public Result<List<ProductVO>> myView() {
        List<Integer> productIds = getInteractionProductIds(InteractionEnum.VIEW);
        if (productIds.isEmpty()) {
            return ApiResult.success(Collections.emptyList());
        }
        return interactionService.myView();
    }
    private ProductVO getProductById(Integer productId) {
        ProductQueryDto queryDto = new ProductQueryDto();
        queryDto.setId(productId);
        List<ProductVO> products = productMapper.query(queryDto);
        return CollectionUtils.isEmpty(products) ? null : products.get(0);
    }

    private boolean isInteractionExist(Integer productId, InteractionEnum type) {
        InteractionQueryDto queryDto = createInteractionQueryDto(productId, type);
        return interactionMapper.queryCount(queryDto) > 0;
    }

    private boolean isCurrentUserProduct(ProductVO product) {
        return Objects.equals(product.getUserId(), LocalThreadHolder.getUserId());
    }

    private void cancelCollection(Integer productId) {
        InteractionQueryDto queryDto = createInteractionQueryDto(productId, InteractionEnum.SAVE);
        List<Integer> ids = interactionMapper.query(queryDto).stream()
                .map(Interaction::getId)
                .collect(Collectors.toList());

        if (!ids.isEmpty()) {
            interactionMapper.batchDelete(ids);
        }
    }


    private List<Integer> getInteractionProductIds(InteractionEnum type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type.getType());
        return interactionMapper.query(queryDto).stream()
                .map(Interaction::getProductId)
                .collect(Collectors.toList());
    }


    private InteractionQueryDto createInteractionQueryDto(Integer productId, InteractionEnum type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type.getType());
        queryDto.setProductId(productId);
        return queryDto;
    }

}
