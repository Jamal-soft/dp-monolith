package ma.inpt.donationService.donor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DonorRepository extends JpaRepository<DonorEntity,Long> {
    @Query("select sum(d.amount) from DonationEntity d where d.donorId=:id")
    Long getSumOfDonationsByDonorId(Long id);

    DonorEntity findByEmail(String email);

}
