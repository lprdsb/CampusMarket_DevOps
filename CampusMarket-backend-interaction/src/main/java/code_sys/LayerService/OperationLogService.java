package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.OperationLogQueryDto;
import code_sys.Po.Entity.OperationLog;
import code_sys.Po.Vo.OperationLogVO;

import java.util.List;

/**
 * 操作日志的业务逻辑接口
 */
public interface OperationLogService {

    Result<String> save(OperationLog operationLog);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<OperationLogVO>> query(OperationLogQueryDto operationLogQueryDto);

}
