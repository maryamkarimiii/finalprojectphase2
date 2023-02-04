package ir.maktab.finalprojectphase2.service;


import ir.maktab.finalprojectphase2.data.dto.ChangePasswordDTO;
import ir.maktab.finalprojectphase2.data.model.*;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomerService extends BaseService<Customer> {
    Customer login(String username, String password);

    Customer findByUsername(String username);

    void changePassword(ChangePasswordDTO changePasswordDTO, String customerUsername);

    List<Customer> findAllCustomer();

    List<Service> seeServicesToChose();

    List<SubService> seeSubServicesToChose(String serviceName);

    void createOrder(Order order);

    List<Offer> seeOrderOffers(String orderTrackingNumber, String propertyToSort, Sort.Direction direction);

    void changeOrderStatusTOStartedByCustomer(String orderTrackingNumber);

    void changeOrderStatusTOFinishedByCustomer(String orderTrackingNumber);

    void confirmOffer(OfferId offerId);
}
