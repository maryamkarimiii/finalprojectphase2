package ir.maktab.finalprojectphase2.data.dao;

import ir.maktab.finalprojectphase2.data.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceDao extends JpaRepository<Service, Long> {

    Optional<Service> findByNameAndDisable(String name, boolean disable);

    List<Service> findAllByDisable(boolean disable);

    boolean existsByName(String name);

}
