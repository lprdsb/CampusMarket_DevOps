package cn.service;

import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.UserQueryDto;
import cn.pojo.dto.update.UserLoginDTO;
import cn.pojo.dto.update.UserRegisterDTO;
import cn.pojo.dto.update.UserUpdateDTO;
import cn.pojo.entity.User;
import cn.pojo.vo.UserVO;

import java.util.List;
import java.util.Map;

public interface UserService {
    Result<String> register(UserRegisterDTO userRegisterDTO);

    Result<Object> login(UserLoginDTO userLoginDTO);

    Result<UserVO> auth();

    Result<List<User>> query(UserQueryDto userQueryDto);

    Result<String> update(UserUpdateDTO userUpdateDTO);

    Result<String> batchDelete(List<Integer> ids);

    Result<String> updatePwd(Map<String, String> map);

    Result<UserVO> getById(Integer id);

    Result<String> insert(UserRegisterDTO userRegisterDTO);

    Result<String> backUpdate(User user);

}
