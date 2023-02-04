package ir.maktab.finalprojectphase2.data.dao;

import ir.maktab.finalprojectphase2.data.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDao extends JpaRepository<Admin, Long> {
}
