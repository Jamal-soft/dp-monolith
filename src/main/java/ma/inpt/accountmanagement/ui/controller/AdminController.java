package ma.inpt.accountmanagement.ui.controller;

import com.cloudinary.Cloudinary;
import ma.inpt.accountmanagement.io.entity.AdminEntity;
import ma.inpt.accountmanagement.io.repository.AdminRepository;
import ma.inpt.accountmanagement.ui.model.request.AdminCreateRequestModel;
import ma.inpt.accountmanagement.ui.model.response.AdminResponseCreation;
import ma.inpt.accountmanagement.ui.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
public class AdminController {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    private Utils utils;

    public AdminController( Utils utils) {
        this.utils = utils;
    }


    @PostMapping("/admins")
    public AdminResponseCreation createAdmin(@ModelAttribute AdminCreateRequestModel adminCreateRequestModel) throws Exception {
        if (adminRepository.findByEmail(adminCreateRequestModel.getEmail()) != null) throw new Exception("user already exists");
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setEmail(adminCreateRequestModel.getEmail());
        adminEntity.setName(adminCreateRequestModel.getName());
        adminEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(adminCreateRequestModel.getPassword()));
        MultipartFile image = adminCreateRequestModel.getImage();
        if (image!=null){
            String path = utils.uploadFile(image);
            if (path!=null){
                adminEntity.setImage(path);
            }
        }

        AdminEntity adminEntity1= adminRepository.save(adminEntity);
        ModelMapper modelMapper = new ModelMapper();
        AdminResponseCreation creation = modelMapper.map(adminEntity1,AdminResponseCreation.class);
        return creation;


    }


}
