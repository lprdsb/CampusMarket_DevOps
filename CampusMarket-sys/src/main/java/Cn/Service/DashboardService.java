package Cn.Service;

import Cn.Poto.Api.Result;
import Cn.Poto.Vo.ChartVO;

import java.util.List;

/**
 * 仪表盘业务逻辑接口
 */
public interface DashboardService {

    Result<List<ChartVO>> staticCount();

    Result<List<ChartVO>> productShelvesInfo(Integer day);

}
