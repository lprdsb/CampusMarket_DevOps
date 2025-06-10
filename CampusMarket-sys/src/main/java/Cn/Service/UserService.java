package Cn.Service;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.UserQueryDto;
import Cn.Poto.Dto.update.UserLoginDTO;
import Cn.Poto.Dto.update.UserRegisterDTO;
import Cn.Poto.Dto.update.UserUpdateDTO;
import Cn.Poto.Entity.User;
import Cn.Poto.Vo.UserVO;

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
