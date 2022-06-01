package ma.inpt.organisationService.organisation;

import ma.inpt.accountmanagement.exceptions.OrganisationServiceException;
import ma.inpt.accountmanagement.shared.OrganisationDto;
import ma.inpt.accountmanagement.ui.model.request.OrganisationDetailsRequestModel;
import ma.inpt.accountmanagement.ui.model.response.OrganisationResp;
import ma.inpt.organisationService.model.request.OrganisationUpdateRequestModel;
import ma.inpt.organisationService.organisation.entity.Organisation;
import ma.inpt.organisationService.model.response.OrganisationResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OrganisationController {
    @Autowired
    OrganisationService organisationService;

    @GetMapping("/organisations/pagination")
    public List<OrganisationResp> getOrganisations(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "limit", defaultValue = "25") int limit){
        List<OrganisationDto> organisations= organisationService.getOrganisations(page,limit);
        List<OrganisationResp> returnValue = new ArrayList<>();

        for (OrganisationDto organisationDto: organisations){
            OrganisationResp organisationResp= new OrganisationResp();
            BeanUtils.copyProperties(organisationDto,organisationResp);
            returnValue.add(organisationResp);
        }



        return returnValue;

    }


    @PostMapping("/signup/organisations")
    public OrganisationResp createOrganisation(@ModelAttribute OrganisationDetailsRequestModel organisationDetailsRequestModel) throws Exception{
        if (organisationDetailsRequestModel.getEmail().isEmpty()) throw new OrganisationServiceException("missing required field");
        ModelMapper modelMapper = new ModelMapper();
        Organisation createdOrganisation= organisationService.createOrganisation(organisationDetailsRequestModel);
        OrganisationResp returnValue = modelMapper.map(createdOrganisation,OrganisationResp.class);
        //BeanUtils.copyProperties(createdUser,returnValue);

        return returnValue;

    }


    @GetMapping(path = "/organisations")
    public List<OrganisationResponseModel> getAllOrganisations(){
         return organisationService.getAllOrganisations();
    }

    @GetMapping(path = "/organisations/{id}")
    public OrganisationResponseModel getOrganisationById(@PathVariable Long id){
         return organisationService.getOrganisationById(id);
    }

    @GetMapping(path = "/organisations/verified")
    public List<OrganisationResponseModel> getOrganisationsVerified(){
        return organisationService.getOrganisationsVerified();
    }

    @GetMapping(path = "/organisations/notverified")
    public List<OrganisationResponseModel> getOrganisationsNotVerified(){
         return organisationService.getOrganisationsNotVerified();
    }
    @DeleteMapping (path = "/organisations/{orgId}")
    public String deleteOrganisationById(@PathVariable(name = "orgId") Long orgId){
        return organisationService.deleteOrganisationById(orgId);
    }
    @PutMapping  (path = "/organisations/{orgId}")
    public String acceptOrganisation(@PathVariable(name = "orgId") Long orgId){
        return organisationService.acceptOrganisation(orgId);
    }

    @PutMapping  (path = "/organisations/profile/{orgId}")
    public String updateOrganisation(@PathVariable(name = "orgId") Long orgId, @ModelAttribute OrganisationUpdateRequestModel organisationUpdateRequestModel){
        return organisationService.updateOrganisation(orgId,organisationUpdateRequestModel);
    }

}
