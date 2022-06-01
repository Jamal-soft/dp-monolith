package ma.inpt.donationService.donation;


import ma.inpt.donationService.model.DonationRequestModel;

import ma.inpt.donationService.model.DonationResponseModelToCalculateSumOfDonations;
import ma.inpt.donationService.model.DonationResponseModelToShowInDashboardOfADonor;
import ma.inpt.donationService.model.DonationResponseSumOfDonationsOfADonorPerOrganisation;
import ma.inpt.organisationService.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class DonationService {
    @Autowired
    DonationRepository donationRepository;

    @Autowired
    ProjectService projectService;

    public List<DonationEntity> getDonationByOrgId(Long id) {
        return donationRepository.findAllByOrgId(id);
    }

    public DonationEntity createDonation(DonationRequestModel donationRequestModel) {
        DonationEntity donationEntity = new DonationEntity();
        donationEntity.setDonorId(donationRequestModel.getDonorId());
        donationEntity.setOrgId(donationRequestModel.getOrgId());
        donationEntity.setAmount(donationRequestModel.getAmount());
        donationEntity.setProjectId(donationRequestModel.getProjectId());
        donationEntity.setDate(new Date());
        donationRepository.save(donationEntity);
        String str = projectService.updateCurrentBalance(donationRequestModel.getProjectId(),donationRequestModel.getAmount());
        System.out.print(str);
        return donationRepository.save(donationEntity);
    }

    public List<DonationResponseModelToCalculateSumOfDonations> getSomeOfDonationsPerDonors() {
        List list = donationRepository.getSomeOfDonationsPerDonors();

        return list;
    }

    public Long getTotalSomeOfDonations() {
        return donationRepository.getTotalSomeOfDonations();
    }

    public List<DonationResponseModelToShowInDashboardOfADonor> getDonationsOfADonorAndShowItInItsDashboard(Long donorId) {
        return donationRepository.getDonationsOfADonorAndShowItInItsDashboard(donorId);
    }

    public List<DonationResponseSumOfDonationsOfADonorPerOrganisation> getSumOfDonationsOfADonorPerOrganisation(Long donorId) {
        return donationRepository.getSumOfDonationsOfADonorPerOrganisation(donorId);
    }
}
