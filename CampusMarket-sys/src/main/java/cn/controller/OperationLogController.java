package cn.controller;

import cn.aop.Pager;
import cn.aop.Protector;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.OperationLogQueryDto;
import cn.pojo.vo.OperationLogVO;
import cn.service.OperationLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志控制器
 */
@RestController
@RequestMapping("/operation-log")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    /**
     * 批量删除
     */
    @Protector(role = "管理员")
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return operationLogService.batchDelete(ids);
    }

    /**
     * 查询
     *
     * @param operationLogQueryDto 查询参数
     * @return Result<List < OperationLogVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<OperationLogVO>> query(@RequestBody OperationLogQueryDto operationLogQueryDto) {
        return operationLogService.query(operationLogQueryDto);
    }
}
