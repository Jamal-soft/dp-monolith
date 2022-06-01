package ma.inpt.accountmanagement.io.repository;

import ma.inpt.accountmanagement.io.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity,Long> {
    AdminEntity findByEmail(String email);
}
