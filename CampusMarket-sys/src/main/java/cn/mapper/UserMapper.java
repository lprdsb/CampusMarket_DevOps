package cn.mapper;

import cn.pojo.dto.query.extend.UserQueryDto;
import cn.pojo.entity.User;
import cn.pojo.vo.UserVO;

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
