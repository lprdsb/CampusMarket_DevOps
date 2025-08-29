package code_sys.LayerMap;

import code_sys.Po.Dto.query.sons.UserQueryDto;
import code_sys.Po.Entity.User;
import code_sys.Po.Vo.UserVO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LayerUserMapper {

    int insert(User userInsert);

    List<User> query(UserQueryDto userQueryDto);

    int queryCount(UserQueryDto userQueryDto);

    int update(User user);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    User getByActive(User user);

    List<UserVO> queryUserList(@Param(value = "ids") List<Integer> ids);
}
