package cn.service.impl;

import cn.context.LocalThreadHolder;
import cn.mapper.UserMapper;
import cn.pojo.api.ApiResult;
import cn.pojo.api.PageResult;
import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.UserQueryDto;
import cn.pojo.dto.update.UserLoginDTO;
import cn.pojo.dto.update.UserRegisterDTO;
import cn.pojo.dto.update.UserUpdateDTO;
import cn.pojo.em.LoginStatusEnum;
import cn.pojo.em.RoleEnum;
import cn.pojo.em.WordStatusEnum;
import cn.pojo.entity.User;
import cn.pojo.vo.UserVO;
import cn.service.UserService;
import cn.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Result<String> register(UserRegisterDTO userRegisterDTO) {
        User entity = userMapper.getByActive(
                User.builder().userAccount(userRegisterDTO.getUserAccount()).build());
        if (Objects.nonNull(entity)) {
            return ApiResult.error("账号不可用");
        }
        User saveEntity = User.builder()
                .userRole(RoleEnum.USER.getRole())
                .userName(userRegisterDTO.getUserName())
                .userAccount(userRegisterDTO.getUserAccount())
                .userAvatar(userRegisterDTO.getUserAvatar())
                .userPwd(userRegisterDTO.getUserPwd())
                .userEmail(userRegisterDTO.getUserEmail())
                .createTime(LocalDateTime.now())
                .isLogin(LoginStatusEnum.USE.getFlag())
                .isWord(WordStatusEnum.USE.getFlag()).build();
        userMapper.insert(saveEntity);
        return ApiResult.success("注册成功");
    }

    @Override
    public Result<Object> login(UserLoginDTO userLoginDTO) {
        User user = userMapper.getByActive(
                User.builder().userAccount(userLoginDTO.getUserAccount()).build());
        if (!Objects.nonNull(user)) {
            return ApiResult.error("账号不存在");
        }
        if (!Objects.equals(userLoginDTO.getUserPwd(), user.getUserPwd())) {
            return ApiResult.error("密码错误");
        }
        if (user.getIsLogin()) {
            return ApiResult.error("登录状态异常");
        }
        String token = JwtUtil.toToken(user.getId(), user.getUserRole());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("role", user.getUserRole());
        User userEntity = new User();
        userEntity.setId(user.getId());
        userEntity.setLastLoginTime(LocalDateTime.now());
        userMapper.update(userEntity);
        return ApiResult.success("登录成功", map);
    }

    @Override
    public Result<UserVO> auth() {
        Integer userId = LocalThreadHolder.getUserId();
        User queryEntity = User.builder().id(userId).build();
        User user = userMapper.getByActive(queryEntity);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ApiResult.success(userVO);
    }

    @Override
    public Result<List<User>> query(UserQueryDto userQueryDto) {
        List<User> users = userMapper.query(userQueryDto);
        Integer count = userMapper.queryCount(userQueryDto);
        return PageResult.success(users, count);
    }

    @Override
    public Result<String> update(UserUpdateDTO userUpdateDTO) {
        User updateEntity = User.builder().id(LocalThreadHolder.getUserId()).build();
        BeanUtils.copyProperties(userUpdateDTO, updateEntity);
        userMapper.update(updateEntity);
        return ApiResult.success();
    }

    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        userMapper.batchDelete(ids);
        return ApiResult.success();
    }

    @Override
    public Result<String> updatePwd(Map<String, String> map) {
        String oldPwd = map.get("oldPwd");
        String newPwd = map.get("newPwd");
        String againPwd = map.get("againPwd");
        if (Objects.isNull(oldPwd)) {
            return ApiResult.error("原始密码输入不能为空");
        }
        if (Objects.isNull(newPwd)) {
            return ApiResult.error("请输入新密码");
        }
        if (Objects.isNull(againPwd)) {
            return ApiResult.error("请补充确认密码");
        }
        if (!newPwd.equals(againPwd)) {
            return ApiResult.error("前后密码输入不一致");
        }
        User user = userMapper.getByActive(
                User.builder().id(LocalThreadHolder.getUserId()).build());
        if (!user.getUserPwd().equals(oldPwd)) {
            return ApiResult.error("原始密码验证失败");
        }
        user.setUserPwd(newPwd);
        userMapper.update(user);
        return ApiResult.success();
    }

    @Override
    public Result<UserVO> getById(Integer id) {
        User user = userMapper.getByActive(User.builder().id(id).build());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ApiResult.success(userVO);
    }

    @Override
    public Result<String> insert(UserRegisterDTO userRegisterDTO) {
        return register(userRegisterDTO);
    }

    @Override
    public Result<String> backUpdate(User user) {
        userMapper.update(user);
        return ApiResult.success();
    }
}
