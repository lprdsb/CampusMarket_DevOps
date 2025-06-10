package code_sys.LayerService.Impl;

import code_sys.LayerEnvironment.LocalThreadHolder;
import code_sys.LayerMap.LayerUserMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.PageResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.UserQueryDto;
import code_sys.Po.Dto.update.UserLoginDTO;
import code_sys.Po.Dto.update.UserRegisterDTO;
import code_sys.Po.Dto.update.UserUpdateDTO;
import code_sys.Po.Em.LoginStatusEnum;
import code_sys.Po.Em.RoleEnum;
import code_sys.Po.Em.WordStatusEnum;
import code_sys.Po.Entity.User;
import code_sys.Po.Vo.UserVO;
import code_sys.LayerService.UserService;
import code_sys.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class LayerUserServiceImpl implements UserService {

    @Resource
    private LayerUserMapper layerUserMapper;

    @Override
    public Result<String> register(UserRegisterDTO userRegisterDTO) {
        if (isAccountExists(userRegisterDTO.getUserAccount())) {
            return ApiResult.error("账号不可用");
        }

        User newUser = buildUserFromRegisterDTO(userRegisterDTO);
        layerUserMapper.insert(newUser);

        return ApiResult.success("注册成功");
    }

    private boolean isAccountExists(String userAccount) {
        User existingUser = layerUserMapper.getByActive(User.builder().userAccount(userAccount).build());
        return existingUser != null;
    }

    private User buildUserFromRegisterDTO(UserRegisterDTO dto) {
        return User.builder()
                .userRole(RoleEnum.USER.getRole())
                .userName(dto.getUserName())
                .userAccount(dto.getUserAccount())
                .userAvatar(dto.getUserAvatar())
                .userPwd(dto.getUserPwd())
                .userEmail(dto.getUserEmail())
                .createTime(LocalDateTime.now())
                .isLogin(LoginStatusEnum.USE.getFlag())
                .isWord(WordStatusEnum.USE.getFlag())
                .build();
    }

    @Override
    public Result<Object> login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getUserAccount();
        String inputPassword = userLoginDTO.getUserPwd();

        // 1. 查询用户是否存在
        User user = layerUserMapper.getByActive(User.builder().userAccount(account).build());
        if (user == null) {
            return ApiResult.error("账号不存在");
        }

        // 2. 校验密码（建议加密比对，如使用 BCrypt）
        if (!Objects.equals(inputPassword, user.getUserPwd())) {
            return ApiResult.error("密码错误");
        }

        // 3. 校验用户是否可登录
        if (Boolean.TRUE.equals(user.getIsLogin())) {
            return ApiResult.error("登录状态异常");
        }

        // 4. 生成 token
        String token = JwtUtil.createToken(user.getId(), user.getUserRole());

        // 5. 更新最后登录时间
        layerUserMapper.update(User.builder()
                .id(user.getId())
                .lastLoginTime(LocalDateTime.now())
                .build());

        // 6. 构造返回数据
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getUserRole());

        return ApiResult.success("登录成功", response);
    }

    @Override
    public Result<UserVO> auth() {
        Integer userId = LocalThreadHolder.getUserId();

        if (userId == null) {
            return ApiResult.error("用户未登录或身份信息缺失");
        }

        User user = layerUserMapper.getByActive(User.builder().id(userId).build());
        if (user == null) {
            return ApiResult.error("用户不存在或已被禁用");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        return ApiResult.success(userVO);
    }

    /**
     * 分页查询用户数据
     *
     * @param userQueryDto 分页参数
     * @return Result<List < User>> 响应结果
     */
    @Override
    public Result<List<User>> query(UserQueryDto userQueryDto) {
        List<User> users = layerUserMapper.query(userQueryDto);
        Integer count = layerUserMapper.queryCount(userQueryDto);
        return PageResult.success(users, count);
    }

    /**
     * 用户信息修改
     *
     * @param userUpdateDTO 修改信息入参
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> update(UserUpdateDTO userUpdateDTO) {
        User updateEntity = User.builder().id(LocalThreadHolder.getUserId()).build();
        BeanUtils.copyProperties(userUpdateDTO, updateEntity);
        layerUserMapper.update(updateEntity);
        return ApiResult.success();
    }

    /**
     * 批量删除用户信息
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        layerUserMapper.batchDelete(ids);
        return ApiResult.success();
    }

    @Override
    public Result<String> updatePwd(Map<String, String> map) {
        String oldPwd = map.get("oldPwd");
        String newPwd = map.get("newPwd");
        String confirmPwd = map.get("againPwd");

        // 参数校验
        if (isBlank(oldPwd)) {
            return ApiResult.error("原始密码不能为空");
        }
        if (isBlank(newPwd)) {
            return ApiResult.error("新密码不能为空");
        }
        if (isBlank(confirmPwd)) {
            return ApiResult.error("请确认新密码");
        }
        if (!newPwd.equals(confirmPwd)) {
            return ApiResult.error("两次输入的新密码不一致");
        }

        // 获取当前用户信息
        Integer userId = LocalThreadHolder.getUserId();
        if (userId == null) {
            return ApiResult.error("用户未登录");
        }

        User user = layerUserMapper.getByActive(User.builder().id(userId).build());
        if (user == null) {
            return ApiResult.error("用户不存在或已被禁用");
        }

        // 校验旧密码
        if (!Objects.equals(oldPwd, user.getUserPwd())) {
            return ApiResult.error("原始密码不正确");
        }

        // 更新密码
        user.setUserPwd(newPwd);
        layerUserMapper.update(user);

        return ApiResult.success("密码修改成功");
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     */
    @Override
    public Result<UserVO> getById(Integer id) {
        User user = layerUserMapper.getByActive(User.builder().id(id).build());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ApiResult.success(userVO);
    }

    /**
     * 后台新增用户
     *
     * @param userRegisterDTO 注册入参
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> insert(UserRegisterDTO userRegisterDTO) {
        return register(userRegisterDTO);
    }

    /**
     * 后台用户信息修改
     *
     * @param user 信息实体
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> backUpdate(User user) {
        layerUserMapper.update(user);
        return ApiResult.success();
    }

}
