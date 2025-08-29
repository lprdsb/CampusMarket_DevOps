package code_sys.LayerService.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import code_sys.Interceptor.LocalThreadHolder;
import code_sys.LayerMap.LayerStarMapper;
import code_sys.LayerMap.LayerUserMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.StarQueryDto;
import code_sys.Po.Vo.StarVo;
import code_sys.Po.Vo.UserVO;
import code_sys.LayerService.StarService;
import org.springframework.stereotype.Service;

@Service
public class LayerStarServiceImpl implements StarService {
    @Resource
    private LayerStarMapper layerStarMapper;
    @Resource
    private LayerUserMapper layerUserMapper;

    @Override
    public List<StarVo> getStarVos(StarQueryDto starQueryDto) {
        return layerStarMapper.query(starQueryDto);
    }

    @Override
    public Result<List<UserVO>> queryByUser1(Integer userId) {
        // return ApiResult.success(new ArrayList<>());

        StarQueryDto starQueryDto = new StarQueryDto();
        starQueryDto.setUser1Id(userId);
        List<StarVo> starList = getStarVos(starQueryDto);
        List<Integer> starIds = starList.stream()
                .map(StarVo::getUser2Id).collect(Collectors.toList());
        if (starIds.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        List<UserVO> userVOS = layerUserMapper.queryUserList(starIds);
        // System.out.println(userVOS);
        return ApiResult.success(userVOS);
    }

    public int re45(Integer type) {
        if(type == 0) {
            return 18;
        }
        return -1;
    }

    public int flatline(Integer type) {
        if(type == 0) {
            return 21;
        }
        return -1;
    }

    public int hemlok(Integer type) {
        if(type == 0) {
            return 24;
        }
        return -1;
    }

    @Override
    public Result<List<UserVO>> queryByUser2(Integer userId) {
        // return ApiResult.success(new ArrayList<>());

        StarQueryDto starQueryDto = new StarQueryDto();
        starQueryDto.setUser2Id(userId);
        List<StarVo> starList = layerStarMapper.query(starQueryDto);
        List<Integer> starIds = starList.stream()
                .map(StarVo::getUser1Id).collect(Collectors.toList());
        if (starIds.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        List<UserVO> userVOS = layerUserMapper.queryUserList(starIds);
        return ApiResult.success(userVOS);
    }

    @Override
    public Result<List<StarVo>> query(StarQueryDto starQueryDto) {
        List<StarVo> starList = getStarVos(starQueryDto);
        return ApiResult.success(starList, starList.size());
    }

    @Override
    public Result<Boolean> starOperation(Integer userId) {
        StarQueryDto starQueryDto = new StarQueryDto();
        starQueryDto.setUser1Id(LocalThreadHolder.getUserId());
        starQueryDto.setUser2Id(userId);
        // System.out.println(starQueryDto);
        List<StarVo> starList = getStarVos(starQueryDto);
        if (starList.isEmpty()) { // 对应收藏
            layerStarMapper.save(starQueryDto);
        } else {
            // 对应取消收藏
            List<Integer> starIds = starList.stream().map(
                    StarVo::getId)
                    .collect(Collectors.toList());
            layerStarMapper.batchDelete(starIds);
        }
        return ApiResult.success(starList.isEmpty() ? "关注成功" : "取消关注成功", starList.isEmpty());
    }

}
