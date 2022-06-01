package ma.inpt.accountmanagement.shared;

import lombok.Data;

@Data
public class OrganisationDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Long rib;
    private String category;
    private boolean verified;
    private byte[] verificationFile;
    private String Location;
    private String description;

}
