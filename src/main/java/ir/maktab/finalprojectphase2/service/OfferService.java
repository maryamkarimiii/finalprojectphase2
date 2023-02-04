package ir.maktab.finalprojectphase2.service;

import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Offer;
import ir.maktab.finalprojectphase2.data.model.OfferId;
import ir.maktab.finalprojectphase2.data.model.Order;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OfferService extends BaseService<Offer> {

    List<Offer> findAllByOrder(Order order, String propertyToSort, Sort.Direction direction);

    Offer findByOrderAndConfirmed(Order order);

    Offer findById(OfferId id);

    boolean isExistByOrder(Order order);

    boolean isExistByOfferId(OfferId offerId);


    void updateOfferAfterConfirmed(Offer offer);
}
