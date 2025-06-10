package Cn.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import Cn.Context.LocalThreadHolder;
import Cn.Mapper.StarMapper;
import Cn.Mapper.UserMapper;
import Cn.Poto.Api.ApiResult;
import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.StarQueryDto;
import Cn.Poto.Vo.StarVo;
import Cn.Poto.Vo.UserVO;
import Cn.Service.StarService;
import org.springframework.stereotype.Service;

@Service
public class StarServiceImpl implements StarService {
    @Resource
    private StarMapper starMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<StarVo> getStarVos(StarQueryDto starQueryDto) {
        return starMapper.query(starQueryDto);
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
        List<UserVO> userVOS = userMapper.queryUserList(starIds);
        // System.out.println(userVOS);
        return ApiResult.success(userVOS);
    }

    @Override
    public Result<List<UserVO>> queryByUser2(Integer userId) {
        // return ApiResult.success(new ArrayList<>());

        StarQueryDto starQueryDto = new StarQueryDto();
        starQueryDto.setUser2Id(userId);
        List<StarVo> starList = starMapper.query(starQueryDto);
        List<Integer> starIds = starList.stream()
                .map(StarVo::getUser1Id).collect(Collectors.toList());
        if (starIds.isEmpty()) {
            return ApiResult.success(new ArrayList<>());
        }
        List<UserVO> userVOS = userMapper.queryUserList(starIds);
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
            starMapper.save(starQueryDto);
        } else {
            // 对应取消收藏
            List<Integer> starIds = starList.stream().map(
                    StarVo::getId)
                    .collect(Collectors.toList());
            starMapper.batchDelete(starIds);
        }
        return ApiResult.success(starList.isEmpty() ? "关注成功" : "取消关注成功", starList.isEmpty());
    }

}
