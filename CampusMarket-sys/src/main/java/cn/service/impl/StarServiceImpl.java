package cn.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.context.LocalThreadHolder;
import cn.mapper.StarMapper;
import cn.mapper.UserMapper;
import cn.pojo.api.ApiResult;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.StarQueryDto;
import cn.pojo.em.InteractionEnum;
import cn.pojo.entity.Interaction;
import cn.pojo.vo.StarVo;
import cn.pojo.vo.UserVO;
import cn.service.StarService;
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
