package ma.inpt.donationService.paymentStripe;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseModelStripe {
    private String message;
    private boolean success;
}
