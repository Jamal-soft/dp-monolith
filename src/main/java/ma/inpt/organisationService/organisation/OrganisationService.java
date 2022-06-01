package ma.inpt.organisationService.organisation;

import ma.inpt.accountmanagement.exceptions.OrganisationServiceException;
import ma.inpt.accountmanagement.shared.OrganisationDto;
import ma.inpt.accountmanagement.ui.model.request.OrganisationDetailsRequestModel;
import ma.inpt.accountmanagement.ui.utils.Utils;
import ma.inpt.organisationService.model.request.OrganisationUpdateRequestModel;
import ma.inpt.organisationService.organisation.entity.Organisation;
import ma.inpt.organisationService.model.response.OrganisationResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrganisationService {
    @Autowired
    OrganisationRepository organisationRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    private Utils utils;

    public OrganisationService(Utils utils) {
        this.utils = utils;
    }


    public Organisation createOrganisation(OrganisationDetailsRequestModel organisationDetailsRequestModel) {
        if (organisationRepository.findByEmail(organisationDetailsRequestModel.getEmail()) != null) throw new OrganisationServiceException("user already exists");
        ModelMapper modelMapper = new ModelMapper();


        Organisation organisationEntity = modelMapper.map(organisationDetailsRequestModel, Organisation.class);


        organisationEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(organisationDetailsRequestModel.getPassword()));
        MultipartFile image = organisationDetailsRequestModel.getImage();

        if (image!=null){
            String path = utils.uploadFile(image);
            if (path!=null){
                organisationEntity.setImage(path);
            }
        }
        MultipartFile file = organisationDetailsRequestModel.getVerificationFile();
        if (file!=null){
            String path = utils.uploadFile(file);
            if (path!=null){
                organisationEntity.setVerificationFile(path);
            }
        }
        Organisation storedUserDetails = organisationRepository.save(organisationEntity);
        // Send an email message to user to verify their email address



        return storedUserDetails;
    }


    public OrganisationDto getOrganisation(String email) {
        Organisation userEntity = organisationRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        OrganisationDto returnValue = new OrganisationDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }



    public List<OrganisationDto> getOrganisations(int page, int limit) {
        List<OrganisationDto> returnValue = new ArrayList<>();
        if (page > 0) page = page - 1;

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<Organisation> usersPage = organisationRepository.findAll(pageableRequest);
        List<Organisation> organisations = usersPage.getContent();

        for (Organisation organisationEntity : organisations) {
            OrganisationDto userDto = new OrganisationDto();
            BeanUtils.copyProperties(organisationEntity, userDto);
            returnValue.add(userDto);
        }


        return returnValue;

    }



    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Organisation organisationEntity = organisationRepository.findByEmail(email);
        if (organisationEntity == null) throw new UsernameNotFoundException(email);

        return new User(organisationEntity.getEmail(),organisationEntity.getEncryptedPassword(),
                true, true,
                true, true, new ArrayList<>());


    }

    public List<OrganisationResponseModel> getAllOrganisations() {
        ModelMapper modelMapper = new ModelMapper();
        List<OrganisationResponseModel> organisationResponseModels = new ArrayList<>();
        List<Organisation> organisation = organisationRepository.findAll();
        organisation.forEach(p->{
            organisationResponseModels.add(modelMapper.map(p,OrganisationResponseModel.class));
        });
        return organisationResponseModels;

    }


    public OrganisationResponseModel getOrganisationById(Long id) {
        Organisation organisation = organisationRepository.findById(id).get();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(organisation,OrganisationResponseModel.class);


    }
    public List<OrganisationResponseModel> getOrganisationsVerified() {
        List<Organisation> organisations = organisationRepository.findByVerified(true);
        ModelMapper modelMapper = new ModelMapper();
        List<OrganisationResponseModel> organisationResponseModels = new ArrayList<>();
        organisations.forEach(p->{
            organisationResponseModels.add(modelMapper.map(p,OrganisationResponseModel.class));
        });
        return organisationResponseModels;
    }

    public List<OrganisationResponseModel> getOrganisationsNotVerified() {
        List<Organisation> organisations = organisationRepository.findByVerified(false);
        ModelMapper modelMapper = new ModelMapper();
        List<OrganisationResponseModel> organisationResponseModels = new ArrayList<>();
        organisations.forEach(p->{
            organisationResponseModels.add(modelMapper.map(p,OrganisationResponseModel.class));
        });
        return organisationResponseModels;
    }


    public String deleteOrganisationById(Long orgId) {
        try {
            organisationRepository.deleteById(orgId);
        }catch (Exception e){
            return e.getMessage();
        }
        return "organisation with id"+ orgId +" deleted succesfully";
    }

    public String acceptOrganisation(Long orgId) {
        try {
            Organisation org = organisationRepository.findById(orgId).get();
            org.setVerified(true);
            organisationRepository.save(org);
        }catch (Exception e){
            return e.getMessage();
        }
        return "organisation with id "+orgId + " accepted";
    }

    public String updateOrganisation(Long orgId, OrganisationUpdateRequestModel organisationUpdateRequestModel) {
        Optional<Organisation> organisation1 = organisationRepository.findById(orgId);
        if (organisation1.isPresent()){
            Organisation organisation = organisation1.get();
            if (organisationUpdateRequestModel.getName()!=null)
            organisation.setName(organisationUpdateRequestModel.getName());
            if (organisationUpdateRequestModel.getCategory()!=null)
            organisation.setCategory(organisationUpdateRequestModel.getCategory());
            if (organisationUpdateRequestModel.getDescription()!=null)
            organisation.setDescription(organisationUpdateRequestModel.getDescription());
            if (organisationUpdateRequestModel.getLocation()!=null)
            organisation.setLocation(organisationUpdateRequestModel.getLocation());
            if (organisationUpdateRequestModel.getPhoneNumber()!=null)
            organisation.setPhoneNumber(organisationUpdateRequestModel.getPhoneNumber());

            if (organisationUpdateRequestModel.getCurrentPassword()!=null){
                if (organisationUpdateRequestModel.getPassword()!=null){
                    if (bCryptPasswordEncoder.matches(organisationUpdateRequestModel.getCurrentPassword(),organisation.getEncryptedPassword())){
                        organisation.setEncryptedPassword(bCryptPasswordEncoder.encode(organisationUpdateRequestModel.getPassword()));

                    }

                }

            }
            MultipartFile image = organisationUpdateRequestModel.getImage();
            if (image!=null){
                String path = utils.uploadFile(image);
                if (path!=null){
                    organisation.setImage(path);
                }

            }
            organisationRepository.save(organisation);
            return "organisation succesfully updated";
        }

        return "organisation not found";

    }
}
