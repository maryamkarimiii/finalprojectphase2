package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dao.OfferDao;
import ir.maktab.finalprojectphase2.data.model.Offer;
import ir.maktab.finalprojectphase2.data.model.OfferId;
import ir.maktab.finalprojectphase2.data.model.Order;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.exception.ValidationException;
import ir.maktab.finalprojectphase2.service.OfferService;
import ir.maktab.finalprojectphase2.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferDao offerDao;
    @Lazy
    @Autowired
    private OrderService orderService;

    @Override
    public void save(Offer offer) {
        validateOfferWorkDate(offer);
        validateOfferPrice(offer);
        offerDao.save(offer);
        if (!isExistByOrder(offer.getOfferId().getOrder()))
            orderService.changeStatusToWaitingForCustomerChose(offer.getOfferId().getOrder());
    }

    @Override
    public void update(Offer offer) {
        offerDao.save(offer);
    }

    @Override
    public List<Offer> findAllByOrder(Order order, String propertyToSort, Sort.Direction direction) {
        if (!(propertyToSort.equals("offerId.expert.totalScore") || propertyToSort.equals("price")))
            throw new ValidationException("you can sort result just by expert.score or price");
        return offerDao
                .findAllByOfferId_OrderAndDisableFalse(order, Sort.by(direction, propertyToSort));
    }

    @Override
    public Offer findByOrderAndConfirmed(Order order) {
        return offerDao.findByOfferId_OrderAndConfirmedByCustomerTrue(order)
                .orElseThrow(() -> new NotFoundException("the offer by this order dose not exist"));
    }

    @Override
    public Offer findById(OfferId id) {
        return offerDao.findById(id).orElseThrow(() -> new NotFoundException("the offer dose not exist"));
    }

    @Override
    public boolean isExistByOrder(Order order) {
        return offerDao.existsByOfferId_Order(order);
    }

    @Override
    public boolean isExistByOfferId(OfferId offerId) {
        return offerDao.existsById(offerId);
    }

    @Override
    public void updateOfferAfterConfirmed(Offer offer) {
        offer.setConfirmedByCustomer(true);
        update(offer);
    }

    private void validateOfferPrice(Offer offer) {
        Double basePrice = offer.getOfferId().getOrder().getSubService().getBaseAmount();
        if (offer.getPrice() < basePrice)
            throw new ValidationException("the offered price cant be less than: " + basePrice);
    }

    public void validateOfferWorkDate(Offer offer) {
        if (offer.getWorkDate().before(new Date()))
            throw new ValidationException("the offered date cant be before: " + new Date());
    }
}
