package Cn.Service;

import java.util.List;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.StarQueryDto;
import Cn.Poto.Vo.StarVo;
import Cn.Poto.Vo.UserVO;

public interface StarService {

    Result<List<UserVO>> queryByUser1(Integer userId);

    Result<List<UserVO>> queryByUser2(Integer userId);

    Result<List<StarVo>> query(StarQueryDto starQueryDto);

    Result<Boolean> starOperation(Integer userId);

    public List<StarVo> getStarVos(StarQueryDto starQueryDto);
}
