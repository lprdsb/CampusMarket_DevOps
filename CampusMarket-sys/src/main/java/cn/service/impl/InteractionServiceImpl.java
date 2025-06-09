package cn.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InteractionServiceImpl implements InteractionService {

    private static final String REPEAT_OPERATION_MSG = "请勿重复操作";
    private static final String PRODUCT_NOT_FOUND_MSG = "商品信息查询异常";
    private static final String SELF_OPERATION_MSG = "别自卖自夸!";
    private static final String SUCCESS_MSG = "卖家已感受到你的热情，快下单吧!";
    private static final String COLLECT_SUCCESS_MSG = "收藏成功";
    private static final String CANCEL_COLLECT_MSG = "取消收藏成功";
    private static final String MESSAGE_TEMPLATE = "用户【%s】对你的【%s】感兴趣!";
    private static final String NEW_PRODUCT_TEMPLATE = "你关注的用户【%s】发布了新商品【%s】，快去看看吧!";

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

    @Override
    public Result<String> likeProduct(Integer productId) {
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

        sendLikeNotification(product);

        saveInteraction(productId, InteractionEnum.LOVE);

        return ApiResult.success(SUCCESS_MSG);
    }

    @Override
    public Result<String> newProduct(Integer productId) {
        ProductVO product = getProductById(productId);
        if (product == null) {
            return ApiResult.error(PRODUCT_NOT_FOUND_MSG);
        }

        List<Integer> followerIds = getCurrentUserFollowers();
        if (followerIds.isEmpty()) {
            return ApiResult.success(SUCCESS_MSG);
        }

        sendNewProductNotifications(product, followerIds);

        return ApiResult.success(SUCCESS_MSG);
    }

    @Override
    public Result<String> save(Interaction interaction) {
        interactionMapper.save(interaction);
        return ApiResult.success("互动行为记录成功");
    }

    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            interactionMapper.batchDelete(ids);
        }
        return ApiResult.success("互动行为删除成功");
    }

    @Override
    public Result<Boolean> saveOperation(Integer productId) {
        boolean isCollected = isInteractionExist(productId, InteractionEnum.SAVE);

        if (isCollected) {
            cancelCollection(productId);
            return ApiResult.success(CANCEL_COLLECT_MSG, false);
        } else {
            saveInteraction(productId, InteractionEnum.SAVE);
            return ApiResult.success(COLLECT_SUCCESS_MSG, true);
        }
    }

    @Override
    public Result<List<Interaction>> query(InteractionQueryDto queryDto) {
        int totalCount = interactionMapper.queryCount(queryDto);
        List<Interaction> interactions = interactionMapper.query(queryDto);
        return ApiResult.success(interactions, totalCount);
    }

    @Override
    public Result<List<ProductVO>> queryUser() {
        List<Integer> productIds = getInteractionProductIds(InteractionEnum.SAVE);
        if (productIds.isEmpty()) {
            return ApiResult.success(Collections.emptyList());
        }
        return ApiResult.success(productMapper.queryProductList(productIds));
    }

    @Override
    public Result<Void> view(Integer productId) {
        if (!isInteractionExist(productId, InteractionEnum.VIEW)) {
            saveInteraction(productId, InteractionEnum.VIEW);
        }
        return ApiResult.success();
    }

    @Override
    public Result<List<ProductVO>> myView() {
        List<Integer> productIds = getInteractionProductIds(InteractionEnum.VIEW);
        if (productIds.isEmpty()) {
            return ApiResult.success(Collections.emptyList());
        }
        return ApiResult.success(productMapper.queryProductList(productIds));
    }

    @Override
    public Result<String> batchDeleteInteraction() {
        List<Integer> ids = getInteractionIds(InteractionEnum.VIEW);
        if (!ids.isEmpty()) {
            interactionMapper.batchDelete(ids);
        }
        return ApiResult.success();
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

    private void sendLikeNotification(ProductVO product) {
        User currentUser = getCurrentUser();
        String content = String.format(MESSAGE_TEMPLATE,
                currentUser.getUserName(), product.getName());
        createAndSaveMessage(product.getUserId(), content);
    }

    private void sendNewProductNotifications(ProductVO product, List<Integer> followerIds) {
        String content = String.format(NEW_PRODUCT_TEMPLATE,
                product.getUserName(), product.getName());

        followerIds.forEach(followerId ->
                createAndSaveMessage(followerId, content)
        );
    }

    private void createAndSaveMessage(Integer userId, String content) {
        Message message = new Message();
        message.setUserId(userId);
        message.setIsRead(false);
        message.setCreateTime(LocalDateTime.now());
        message.setContent(content);
        messageMapper.save(message);
    }

    private void saveInteraction(Integer productId, InteractionEnum type) {
        Interaction interaction = new Interaction();
        interaction.setUserId(LocalThreadHolder.getUserId());
        interaction.setType(type.getType());
        interaction.setProductId(productId);
        interaction.setCreateTime(LocalDateTime.now());
        interactionMapper.save(interaction);
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

    private List<Integer> getCurrentUserFollowers() {
        StarQueryDto starQueryDto = new StarQueryDto();
        starQueryDto.setUser2Id(LocalThreadHolder.getUserId());
        return starService.getStarVos(starQueryDto).stream()
                .map(StarVo::getUser1Id)
                .collect(Collectors.toList());
    }

    private List<Integer> getInteractionProductIds(InteractionEnum type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type.getType());
        return interactionMapper.query(queryDto).stream()
                .map(Interaction::getProductId)
                .collect(Collectors.toList());
    }

    private List<Integer> getInteractionIds(InteractionEnum type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type.getType());
        return interactionMapper.query(queryDto).stream()
                .map(Interaction::getId)
                .collect(Collectors.toList());
    }

    private InteractionQueryDto createInteractionQueryDto(Integer productId, InteractionEnum type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type.getType());
        queryDto.setProductId(productId);
        return queryDto;
    }

    private User getCurrentUser() {
        User user = new User();
        user.setId(LocalThreadHolder.getUserId());
        return userMapper.getByActive(user);
    }
}