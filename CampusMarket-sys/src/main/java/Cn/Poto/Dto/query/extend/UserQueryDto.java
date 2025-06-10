package Cn.Poto.Dto.query.extend;

import Cn.Poto.Dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDto extends QueryDto {

    private String userAccount;
    private String userName;
    private String userEmail;
    private Boolean role;
    private Boolean isLogin;
    private Boolean isWord;
}
