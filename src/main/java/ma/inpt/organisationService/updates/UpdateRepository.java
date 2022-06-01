package ma.inpt.organisationService.updates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateRepository extends JpaRepository<UpdateEntity,Long> {
    List<UpdateEntity> findAllByProjectId(Long projectId);
}
