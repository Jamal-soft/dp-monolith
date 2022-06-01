package ma.inpt.accountmanagement.ui.model.request;

import lombok.Data;

@Data
public class OrganisationLoginRequestModel {
    private String email;
    private String password;
}
