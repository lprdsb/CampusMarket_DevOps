package Cn.Mapper;

import Cn.Poto.Dto.query.extend.UserQueryDto;
import Cn.Poto.Entity.User;
import Cn.Poto.Vo.UserVO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    int insert(User userInsert);

    List<User> query(UserQueryDto userQueryDto);

    int queryCount(UserQueryDto userQueryDto);

    int update(User user);

    void batchDelete(@Param(value = "ids") List<Integer> ids);

    User getByActive(User user);

    List<UserVO> queryUserList(@Param(value = "ids") List<Integer> ids);
}
