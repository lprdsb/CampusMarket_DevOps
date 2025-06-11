package code_sys.Po.Dto.update;

import code_sys.Po.Dto.query.base.QueryDto;

public class UserPwdUpdateDTO {
    private String oldPwd;
    private String newPwd;
    private String againPwd;

    public String getOldPwd() { return oldPwd; }
    public String getNewPwd() { return newPwd; }
    public String getAgainPwd() { return againPwd; }
}