package ma.inpt.donationService.donor;

import ma.inpt.accountmanagement.exceptions.DonorServiceException;
import ma.inpt.accountmanagement.ui.model.request.DonorDetailRequestModel;
import ma.inpt.accountmanagement.ui.model.request.DonorRequestUpdateProfile;
import ma.inpt.accountmanagement.ui.model.request.ResetPasswordRequestModel;
import ma.inpt.accountmanagement.ui.model.response.DonorResp;
import ma.inpt.donationService.model.DonorModelResponse;
import ma.inpt.donationService.model.DonorModelResponseWithSumOfDonations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DonorController {
    @Autowired
    DonorService donorService;


    @PostMapping(path = "/signup/donors")
    public DonorResp createDonor(@ModelAttribute DonorDetailRequestModel donorDetailRequestModel) throws Exception {
        if (donorDetailRequestModel.getEmail().isEmpty()) throw new DonorServiceException("missing required field");
        ModelMapper modelMapper=new ModelMapper();
        DonorEntity createdDonor = donorService.createDonor(donorDetailRequestModel);
        DonorResp returnValue = modelMapper.map(createdDonor, DonorResp.class);

        return returnValue;
    }
    @PostMapping(path = "/donors/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequestModel resetPasswordRequestModel) throws Exception {
        if (resetPasswordRequestModel.newPass.isEmpty() && resetPasswordRequestModel.currentPass.isEmpty()) throw new DonorServiceException("missing required field");
        return donorService.resetPassword(resetPasswordRequestModel);
    }

    @PutMapping(path = "/donors/update-profile/{donorId}")
    public String updateProfile(@PathVariable("donorId") Long donorId, @ModelAttribute DonorRequestUpdateProfile DonorRequestUpdateProfile) throws Exception {
        return donorService.updateProfile(donorId,DonorRequestUpdateProfile);
    }

    @GetMapping("/donors")
    public List<DonorModelResponse> getDonors(){
        return donorService.getDonors();
    }

    @GetMapping("/donors/{id}")
    public DonorModelResponse getDonorById(@PathVariable Long id){
        return donorService.getDonorById(id);
    }

    @GetMapping("/donors/{id}/sum-donations")
    public DonorModelResponseWithSumOfDonations getSumOfDonationsByDonorId(@PathVariable Long id){
        return donorService.getSumOfDonationsByDonorId(id);
    }


}
