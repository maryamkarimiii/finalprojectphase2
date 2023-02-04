package ir.maktab.finalprojectphase2.data.dao;

import ir.maktab.finalprojectphase2.data.model.Offer;
import ir.maktab.finalprojectphase2.data.model.OfferId;
import ir.maktab.finalprojectphase2.data.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfferDao extends JpaRepository<Offer, OfferId> {
    List<Offer> findAllByOfferId_OrderAndDisableFalse(Order order, Sort sort);

    Optional<Offer> findByOfferId_OrderAndConfirmedByCustomerTrue(Order order);

    boolean existsByOfferId_Order(Order order);
}
