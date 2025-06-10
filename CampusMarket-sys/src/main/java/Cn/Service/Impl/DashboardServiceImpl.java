package Cn.Service.Impl;

import Cn.Mapper.*;
import Cn.Poto.Api.ApiResult;
import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.base.QueryDto;
import Cn.Poto.Dto.query.extend.*;
import Cn.Poto.Vo.ChartVO;
import Cn.Poto.Vo.ProductVO;
import Cn.Service.DashboardService;
import Cn.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仪表盘业务逻辑接口实现类
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private InteractionMapper interactionMapper;
    @Resource
    private EvaluationsMapper evaluationsMapper;

    /**
     * 统计系统的基础数据
     *
     * @return Result<List<ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> staticCount() {
        // 使用统一方法来创建ChartVO列表
        List<ChartVO> chartVOList = new ArrayList<>();
        chartVOList.add(createChartVO("用户数", userMapper.queryCount(new UserQueryDto())));
        chartVOList.add(createChartVO("收录商品", productMapper.queryCount(new ProductQueryDto())));
        chartVOList.add(createChartVO("订单数", ordersMapper.queryCount(new OrdersQueryDto())));
        chartVOList.add(createChartVO("消息通知", messageMapper.queryCount(new MessageQueryDto())));
        chartVOList.add(createChartVO("互动数据", interactionMapper.queryCount(new InteractionQueryDto())));
        chartVOList.add(createChartVO("商品评论", evaluationsMapper.queryCount(new EvaluationsQueryDto())));

        return ApiResult.success(chartVOList);
    }

    /**
     * 统计商品的上架情况
     *
     * @param day 往前查的天数
     * @return Result<List<ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> productShelvesInfo(Integer day) {
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        ProductQueryDto productQueryDto = new ProductQueryDto();
        productQueryDto.setStartTime(queryDto.getStartTime());
        productQueryDto.setEndTime(queryDto.getEndTime());
        List<ProductVO> productVOS = productMapper.query(productQueryDto);

        List<LocalDateTime> dateTimeList = productVOS.stream()
                .map(ProductVO::getCreateTime)
                .collect(Collectors.toList());

        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(day, dateTimeList);
        return ApiResult.success(chartVOS);
    }

    /**
     * 创建ChartVO对象，避免重复代码
     *
     * @param label 显示的标签
     * @param count 数据
     * @return ChartVO
     */
    private ChartVO createChartVO(String label, int count) {
        return new ChartVO(label, count);
    }
}
