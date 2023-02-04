package ir.maktab.finalprojectphase2.data.dao;

import ir.maktab.finalprojectphase2.data.model.SubService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubServiceDao extends JpaRepository<SubService, Long> {

    Optional<SubService> findByNameAndDisable(String name, boolean disable);

    List<SubService> findAllByServiceNameAndDisableFalse(String serviceName);

    List<SubService> findAllByDisable(boolean disable);

    boolean existsByName(String name);
}
