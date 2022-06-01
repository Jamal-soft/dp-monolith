package ma.inpt.accountmanagement.ui.model.response;

import lombok.Data;

@Data
public class DonorResp {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String image;
}
