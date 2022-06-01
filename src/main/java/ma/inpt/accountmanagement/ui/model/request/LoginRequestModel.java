package ma.inpt.accountmanagement.ui.model.request;

import lombok.Data;

@Data
public class LoginRequestModel {
    private String email;
    private String password;
}
