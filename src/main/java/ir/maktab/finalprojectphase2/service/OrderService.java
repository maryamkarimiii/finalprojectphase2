package ir.maktab.finalprojectphase2.service;


import ir.maktab.finalprojectphase2.data.enums.OrderStatus;
import ir.maktab.finalprojectphase2.data.model.Customer;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Order;
import ir.maktab.finalprojectphase2.data.model.SubService;

import java.util.List;

public interface OrderService extends BaseService<Order> {

    List<Order> findAllBySubServiceAndOrderStatus(List<SubService> subServiceList);

    Order findByTrackingNumber(String trackingNumber);

    void changeStatusToWaitingForCustomerChose(Order order);

    void changeStatusToStarted(String orderTrackingNumber);

    void changeStatusToFinished(String orderTrackingNumber);

    void updateOrderAfterOfferConfirmed(Order order, Expert expert);

    List<Order> findAllByCustomer(Customer customer);
    void softDelete(Order order);
}
