package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Vo.ChartVO;

import java.util.List;

/**
 * 仪表盘业务逻辑接口
 */
public interface DashboardService {

    Result<List<ChartVO>> staticCount();

    Result<List<ChartVO>> productShelvesInfo(Integer day);

}
