package ma.inpt.donationService.donor;

import ma.inpt.accountmanagement.exceptions.OrganisationServiceException;
import ma.inpt.accountmanagement.ui.model.request.DonorDetailRequestModel;
import ma.inpt.accountmanagement.ui.model.request.DonorRequestUpdateProfile;
import ma.inpt.accountmanagement.ui.model.request.ResetPasswordRequestModel;
import ma.inpt.accountmanagement.ui.utils.Utils;
import ma.inpt.donationService.model.DonorModelResponse;
import ma.inpt.donationService.model.DonorModelResponseWithSumOfDonations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DonorService {
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    private Utils utils;

    public DonorService(Utils utils) {
        this.utils = utils;
    }

    public DonorEntity createDonor(DonorDetailRequestModel donorDetailRequestModel) {
        if (donorRepository.findByEmail(donorDetailRequestModel.getEmail()) != null) throw new OrganisationServiceException("user already exists");
        DonorEntity donorEntity =new DonorEntity();
        donorEntity.setName(donorDetailRequestModel.getName());
        donorEntity.setPhoneNumber(donorDetailRequestModel.getPhoneNumber());
        donorEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(donorDetailRequestModel.getPassword()));
        donorEntity.setEmail(donorDetailRequestModel.getEmail());
        MultipartFile image = donorDetailRequestModel.getImage();


        if (image!=null){

            String path = utils.uploadFile(image);
            if (path!=null){
                donorEntity.setImage(path);
            }

        }

        return donorRepository.save(donorEntity);
        // Send an email message to user to verify their email address



    }


    public String resetPassword(ResetPasswordRequestModel resetPasswordRequestModel) {
        if (!resetPasswordRequestModel.newPass.equals(resetPasswordRequestModel.newPassAgain)) return "doesn't match ";
        Optional<DonorEntity> donorEntity1 = donorRepository.findById(resetPasswordRequestModel.donorId);
        if (donorEntity1.isPresent()){
            DonorEntity donorEntity = donorEntity1.get();
            if (!bCryptPasswordEncoder.matches(resetPasswordRequestModel.currentPass,donorEntity.getEncryptedPassword())) return "current password is false";
            donorEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(resetPasswordRequestModel.newPass));
            donorRepository.save(donorEntity);
            return "password changed succesfully";
        }
        return "can't find the donor";
    }

    public String updateProfile(Long donorId, DonorRequestUpdateProfile donorRequestUpdateProfile) {
        Optional<DonorEntity> donorEntity1 = donorRepository.findById(donorId);
        if (donorEntity1.isPresent()){
            DonorEntity donorEntity =donorEntity1.get();
            donorEntity.setLocation(donorRequestUpdateProfile.getLocation());
            donorEntity.setName(donorRequestUpdateProfile.getName());
            donorEntity.setPhoneNumber(donorRequestUpdateProfile.getPhoneNumber());

            MultipartFile image = donorRequestUpdateProfile.getImage();


            if (image!=null){

                String path = utils.uploadFile(image);
                if (path!=null){
                    donorEntity.setImage(path);
                }

            }
            donorRepository.save(donorEntity);
            return "profile updated succesfuly";
        }
        return "donor does not exist";
    }

    public List<DonorModelResponse> getDonors() {
        ModelMapper modelMapper  = new ModelMapper();
        List<DonorEntity> donorsEntity =  donorRepository.findAll();
        List<DonorModelResponse> donorModelResponse = new ArrayList<>();

        donorsEntity.forEach(p ->{
            donorModelResponse.add(modelMapper.map(p,DonorModelResponse.class));
        });
    return donorModelResponse;
    }

    public DonorModelResponse getDonorById(Long id) {
        ModelMapper modelMapper  = new ModelMapper();
        DonorEntity donorEntity = donorRepository.findById(id).get();
        DonorModelResponse donorModelResponse = modelMapper.map(donorEntity,DonorModelResponse.class);
        return donorModelResponse;

    }

    public DonorModelResponseWithSumOfDonations getSumOfDonationsByDonorId(Long id) {
        Long aLong= donorRepository.getSumOfDonationsByDonorId(id);
        ModelMapper modelMapper  = new ModelMapper();
        DonorEntity donorEntity = donorRepository.findById(id).get();
        DonorModelResponseWithSumOfDonations donorModelResponse = modelMapper.map(donorEntity,DonorModelResponseWithSumOfDonations.class);
        donorModelResponse.setTotalDonations(aLong);
        return donorModelResponse;
    }
}
