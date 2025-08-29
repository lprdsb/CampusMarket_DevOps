package code_sys.Po.Dto.update;

import code_sys.Po.Dto.query.base.QueryDto;

public class UserPwdUpdateDTO {
    private String oldPwd;
    private String newPwd;
    private String againPwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public String getAgainPwd() {
        return againPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public void setAgainPwd(String againPwd) {
        this.againPwd = againPwd;
    }
}