package code_sys.Po.Dto.query.sons;

import code_sys.Po.Dto.query.base.QueryDto;
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
