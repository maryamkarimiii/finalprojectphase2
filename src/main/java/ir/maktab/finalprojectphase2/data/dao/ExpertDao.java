package ir.maktab.finalprojectphase2.data.dao;

import ir.maktab.finalprojectphase2.data.enums.ExpertRegistrationStatus;
import ir.maktab.finalprojectphase2.data.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpertDao extends JpaRepository<Expert, Long> {

    Optional<Expert> findByUsernameAndDisable(String username, boolean disable);

    boolean existsByUsername(String username);

    List<Expert> findAllByExpertRegistrationStatus(ExpertRegistrationStatus registrationStatus);
}
