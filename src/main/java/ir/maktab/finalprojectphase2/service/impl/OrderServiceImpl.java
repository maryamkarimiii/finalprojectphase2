package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dao.OrderDao;
import ir.maktab.finalprojectphase2.data.enums.OrderStatus;
import ir.maktab.finalprojectphase2.data.model.*;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.exception.ValidationException;
import ir.maktab.finalprojectphase2.service.OfferService;
import ir.maktab.finalprojectphase2.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;

    private final OfferService offerService;
    private static final List<OrderStatus> STATUS_LIST =
            List.of(OrderStatus.WAITING_FOR_OFFER, OrderStatus.WAITING_FOR_CUSTOMER_CHOICE);


    @Override
    public void save(Order order) {
        validateOrderPrice(order);
        validateOrderDate(order);
        order.setOrderStatus(OrderStatus.WAITING_FOR_OFFER);
        orderDao.save(order);
    }

    @Override
    public List<Order> findAllBySubServiceAndOrderStatus(List<SubService> subServiceList) {
        return orderDao
                .findAllBySubServiceInAndOrderStatusInAndDisableFalse(subServiceList, STATUS_LIST);
    }

    @Override
    public Order findByTrackingNumber(String trackingNumber) {
        return orderDao.findByTrackingNumberAndDisable(trackingNumber, false)
                .orElseThrow(() -> new NotFoundException(String.format("the order by this %s number dose not exist", trackingNumber)));
    }

    @Override
    public void update(Order order) {
        orderDao.save(order);
    }

    @Override
    public void changeStatusToWaitingForCustomerChose(Order order) {
        if (!order.getOrderStatus().equals(OrderStatus.WAITING_FOR_OFFER))
            throw new ValidationException("cant change status to waiting for offer wait for first offer");
        order.setOrderStatus(OrderStatus.WAITING_FOR_CUSTOMER_CHOICE);
        orderDao.save(order);
    }

    @Override
    public void changeStatusToStarted(String orderTrackingNumber) {
        Order order = findByTrackingNumber(orderTrackingNumber);
        validateOrderStatusToStarted(order);
        order.setOrderStatus(OrderStatus.START_WORKING);
        orderDao.save(order);
    }

    @Override
    public void changeStatusToFinished(String orderTrackingNumber) {
        Order order = findByTrackingNumber(orderTrackingNumber);
        if (!order.getOrderStatus().equals(OrderStatus.START_WORKING))
            throw new ValidationException("you cant change status to finished before expert start the work");
        order.setOrderStatus(OrderStatus.FINISH_WORKING);
        orderDao.save(order);
    }

    @Override
    public void updateOrderAfterOfferConfirmed(Order order, Expert expert) {
        if (!order.getOrderStatus().equals(OrderStatus.WAITING_FOR_CUSTOMER_CHOICE))
            throw new ValidationException("you cant change status to wait for come before exist of any offered");
        order.setOrderStatus(OrderStatus.WAITING_FOR_COMING);
        order.setExpert(expert);
        orderDao.save(order);
    }

    @Override
    public List<Order> findAllByCustomer(Customer customer) {
        return orderDao.findAllByCustomer(customer);
    }

    @Override
    public void softDelete(Order order) {
        if (order.getOrderStatus().equals(OrderStatus.START_WORKING))
            throw new ValidationException("you can not cancel order because the expert start the work");
        order.setDisable(true);
        orderDao.save(order);
    }

    private void validateOrderPrice(Order order) {
        Double baseAmount = order.getSubService().getBaseAmount();
        if (order.getPrice() < baseAmount)
            throw new ValidationException(String.format("order price cant be less than %f", baseAmount));
    }


    private void validateOrderDate(Order order) {
        if (order.getWorkDate().isBefore(LocalDate.now()))
            throw new ValidationException(String.format("the date cant be before %tc", new Date()));
    }

    private void validateOrderStatusToStarted(Order order) {
        if (!order.getOrderStatus().equals(OrderStatus.WAITING_FOR_COMING))
            throw new ValidationException("you cant change status to coming before confirmed on offer");

        Offer offer = offerService.findByOrderAndConfirmed(order);
        LocalDate offerDate = offer.getWorkDate();
        if (offerDate.isAfter(LocalDate.now()))
            throw new ValidationException
                    (String.format("you cant change order status to startWork before %tc that expert offered", offerDate));
    }
}
