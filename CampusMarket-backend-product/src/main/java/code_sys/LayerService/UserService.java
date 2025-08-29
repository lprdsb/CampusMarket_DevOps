package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.UserQueryDto;
import code_sys.Po.Dto.update.UserLoginDTO;
import code_sys.Po.Dto.update.UserPwdUpdateDTO;
import code_sys.Po.Dto.update.UserRegisterDTO;
import code_sys.Po.Dto.update.UserUpdateDTO;
import code_sys.Po.Entity.User;
import code_sys.Po.Vo.UserVO;

import java.util.List;
import java.util.Map;

public interface UserService {
    Result<String> register(UserRegisterDTO userRegisterDTO);

    Result<Object> login(UserLoginDTO userLoginDTO);

    Result<UserVO> auth();

    Result<List<User>> query(UserQueryDto userQueryDto);

    Result<String> update(UserUpdateDTO userUpdateDTO);

    Result<String> batchDelete(List<Integer> ids);

    public Result<String> updatePwd(UserPwdUpdateDTO dto);

    Result<UserVO> getById(Integer id);

    Result<String> insert(UserRegisterDTO userRegisterDTO);

    Result<String> backUpdate(User user);

}
