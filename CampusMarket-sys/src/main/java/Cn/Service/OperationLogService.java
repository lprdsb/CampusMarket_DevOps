package Cn.Service;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.OperationLogQueryDto;
import Cn.Poto.Entity.OperationLog;
import Cn.Poto.Vo.OperationLogVO;

import java.util.List;

/**
 * 操作日志的业务逻辑接口
 */
public interface OperationLogService {

    Result<String> save(OperationLog operationLog);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<OperationLogVO>> query(OperationLogQueryDto operationLogQueryDto);

}
