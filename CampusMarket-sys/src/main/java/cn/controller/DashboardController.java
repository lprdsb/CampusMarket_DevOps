package cn.controller;

import cn.pojo.api.Result;
import cn.pojo.vo.ChartVO;
import cn.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 仪表盘数据统计
 */
@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 统计系统的基础数据
     *
     * @return Result<List<ChartVO>> 响应结果
     */
    @GetMapping("/staticCount")
    public Result<List<ChartVO>> staticCount() {
        return dashboardService.staticCount();
    }

    /**
     * 统计商品的上架情况
     *
     * @param day 往前查的天数
     * @return Result<List<ChartVO>> 响应结果
     */
    @GetMapping("/productShelvesInfo/{day}")
    public Result<List<ChartVO>> productShelvesInfo(@PathVariable Integer day) {
        return dashboardService.productShelvesInfo(day);
    }
}
