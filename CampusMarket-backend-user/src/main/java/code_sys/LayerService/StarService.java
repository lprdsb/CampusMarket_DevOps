package code_sys.LayerService;

import java.util.List;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.StarQueryDto;
import code_sys.Po.Vo.StarVo;
import code_sys.Po.Vo.UserVO;

public interface StarService {

    Result<List<UserVO>> queryByUser1(Integer userId);

    Result<List<UserVO>> queryByUser2(Integer userId);

    Result<List<StarVo>> query(StarQueryDto starQueryDto);

    Result<Boolean> starOperation(Integer userId);

    public List<StarVo> getStarVos(StarQueryDto starQueryDto);
}
