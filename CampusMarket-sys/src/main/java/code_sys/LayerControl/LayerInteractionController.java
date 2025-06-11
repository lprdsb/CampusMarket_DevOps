package code_sys.LayerControl;

import code_sys.Aop.Pager;
import code_sys.Interceptor.LocalThreadHolder;
import code_sys.LayerMap.LayerInteractionMapper;
import code_sys.LayerMap.LayerMessageMapper;
import code_sys.LayerMap.LayerProductMapper;
import code_sys.LayerMap.LayerUserMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.InteractionQueryDto;
import code_sys.Po.Dto.query.sons.ProductQueryDto;
import code_sys.Po.Em.InteractionType;
import code_sys.Po.Entity.Interaction;
import code_sys.Po.Vo.ProductVO;
import code_sys.LayerService.InteractionService;
import code_sys.LayerService.StarService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/interaction")
public class LayerInteractionController {



    private static final String REPEAT_OPERATION_MSG = "请勿重复操作";
    private static final String PRODUCT_NOT_FOUND_MSG = "商品信息查询异常";
    private static final String SELF_OPERATION_MSG = "别自卖自夸!";
    private static final String CANCEL_COLLECT_MSG = "取消收藏成功";

    @Resource
    private InteractionService interactionService;
    @Resource
    private LayerInteractionMapper layerInteractionMapper;
    @Resource
    private LayerProductMapper layerProductMapper;
    @Resource
    private LayerMessageMapper layerMessageMapper;
    @Resource
    private LayerUserMapper layerUserMapper;
    @Resource
    private StarService starService;

    @PostMapping("/likeProduct/{productId}")
    public Result<String> likeProduct(@PathVariable Integer productId) {
        System.out.println("进入接口");
        ProductVO product = getProductById(productId);

        if (product == null) {
            System.out.println("没找到产品");
            return ApiResult.error(PRODUCT_NOT_FOUND_MSG);
        }

        if (isInteractionExist(productId, InteractionType.LOVE)) {
            System.out.println("交互重复");
            return ApiResult.error(REPEAT_OPERATION_MSG);
        }

        if (isCurrentUserProduct(product)) {
            System.out.println("在操作自己商品");
            return ApiResult.error(SELF_OPERATION_MSG);
        }
        System.out.println("进入返回函数");
        return interactionService.likeProduct(productId);
    }


    @PostMapping("/saveOperation/{productId}")
    public Result<Boolean> saveOperation(@PathVariable Integer productId) {
        System.out.println("进入接口");
        boolean isCollected = isInteractionExist(productId, InteractionType.SAVE);

        if (isCollected) {
            System.out.println("已经收藏");
            cancelCollection(productId);
            return ApiResult.success(CANCEL_COLLECT_MSG, false);
        }
        System.out.println("进入返回函数");
        return interactionService.saveOperation(productId);
    }


    @PostMapping("/batchDeleteView")
    public Result<String> batchDeleteInteraction() {
        System.out.println("进入接口");
        System.out.println("进入返回函数");
        return interactionService.batchDeleteInteraction();
    }


    @PostMapping("/batchDelete")
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        System.out.println("进入接口");
        System.out.println("进入返回函数");
        return interactionService.batchDelete(ids);
    }


    @Pager
    @PostMapping("/query")
    public Result<List<Interaction>> query(@RequestBody InteractionQueryDto interactionQueryDto) {
        System.out.println("进入接口");
        System.out.println("进入返回函数");
        return interactionService.query(interactionQueryDto);
    }

    @PostMapping("/view/{productId}")
    public Result<Void> view(@PathVariable Integer productId) {
        System.out.println("进入接口");
        System.out.println("进入返回函数");
        return interactionService.view(productId);
    }

    @PostMapping("/queryUser")
    public Result<List<ProductVO>> queryUser() {
        System.out.println("进入接口");
        List<Integer> productIds = getInteractionProductIds(InteractionType.SAVE);
        if (productIds.isEmpty()) {
            System.out.println("产品为空");
            return ApiResult.success(Collections.emptyList());
        }
        System.out.println("进入返回函数");
        return interactionService.queryUser();
    }


    @PostMapping("/myView")
    public Result<List<ProductVO>> myView() {
        System.out.println("进入接口");
        List<Integer> productIds = getInteractionProductIds(InteractionType.VIEW);
        if (productIds.isEmpty()) {
            System.out.println("产品为空");
            return ApiResult.success(Collections.emptyList());
        }
        System.out.println("进入返回函数");
        return interactionService.myView();
    }
    private ProductVO getProductById(Integer productId) {
        ProductQueryDto queryDto = new ProductQueryDto();
        queryDto.setId(productId);
        List<ProductVO> products = layerProductMapper.query(queryDto);
        return CollectionUtils.isEmpty(products) ? null : products.get(0);
    }

    private boolean isInteractionExist(Integer productId, InteractionType type) {
        InteractionQueryDto queryDto = createInteractionQueryDto(productId, type);
        return layerInteractionMapper.queryCount(queryDto) > 0;
    }

    private boolean isCurrentUserProduct(ProductVO product) {
        return Objects.equals(product.getUserId(), LocalThreadHolder.getUserId());
    }

    private void cancelCollection(Integer productId) {
        InteractionQueryDto queryDto = createInteractionQueryDto(productId, InteractionType.SAVE);
        List<Integer> ids = layerInteractionMapper.query(queryDto).stream()
                .map(Interaction::getId)
                .collect(Collectors.toList());

        if (!ids.isEmpty()) {
            layerInteractionMapper.batchDelete(ids);
        }
    }


    private List<Integer> getInteractionProductIds(InteractionType type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type.getType());
        return layerInteractionMapper.query(queryDto).stream()
                .map(Interaction::getProductId)
                .collect(Collectors.toList());
    }


    private InteractionQueryDto createInteractionQueryDto(Integer productId, InteractionType type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type.getType());
        queryDto.setProductId(productId);
        return queryDto;
    }

}
