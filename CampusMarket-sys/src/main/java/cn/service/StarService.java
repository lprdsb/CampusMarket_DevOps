package cn.service;

import java.util.List;

import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.StarQueryDto;
import cn.pojo.vo.StarVo;
import cn.pojo.vo.UserVO;

public interface StarService {

    Result<List<UserVO>> queryByUser1(Integer userId);

    Result<List<UserVO>> queryByUser2(Integer userId);

    Result<List<StarVo>> query(StarQueryDto starQueryDto);

    Result<Boolean> starOperation(Integer userId);

    public List<StarVo> getStarVos(StarQueryDto starQueryDto);
}
