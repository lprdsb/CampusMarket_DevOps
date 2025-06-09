package cn.pojo.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    private String userName;
    private String userAccount;
    private String userPwd;
    private String userEmail;
    private String userAvatar;
}
