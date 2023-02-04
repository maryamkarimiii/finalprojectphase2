package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dao.CustomerDao;
import ir.maktab.finalprojectphase2.data.dto.ChangePasswordDTO;
import ir.maktab.finalprojectphase2.data.enums.Role;
import ir.maktab.finalprojectphase2.data.model.*;
import ir.maktab.finalprojectphase2.exception.NotCorrectInputException;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;
    private final OrderService orderService;
    private final OfferService offerService;


    @Override
    public void save(Customer customer) {
        customer.setUsername(customer.getEmail());
        customer.setRole(Role.CUSTOMER);
        customer.setWallet(new Wallet(0.0));
        customerDao.save(customer);
    }

    @Override
    public Customer login(String username, String password) {
        return customerDao.findByUsername(username)
                .filter(customer -> customer.getPassword().equals(password))
                .orElseThrow(() -> new NotCorrectInputException
                        (String.format("the %s username or %s password is incorrect", username, password)));
    }

    @Override
    public Customer findByUsername(String username) {
        return customerDao.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("the customer %s dose not exist", username)));
    }

    @Override
    public List<Customer> findAllCustomer() {
        return customerDao.findAll();
    }

    @Override
    public void update(Customer customer) {
        customerDao.save(customer);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO, String customerUsername) {
        Customer customer = findByUsername(customerUsername);
        if (!customer.getPassword().equals(changePasswordDTO.getOldPassword()))
            throw new NotCorrectInputException("the password is not correct");
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword()))
            throw new NotCorrectInputException("the new password and confirm password are not same");
        customer.setPassword(changePasswordDTO.getNewPassword());
        update(customer);
    }

    @Override
    public List<Service> seeServicesToChose() {
        return serviceService.findAllEnableService();
    }

    @Override
    public List<SubService> seeSubServicesToChose(String serviceName) {
        return subServiceService.findAllByServiceName(serviceName);
    }

    @Override
    public void createOrder(Order order) {
        orderService.save(order);
    }

    @Override
    public List<Offer> seeOrderOffers(String orderTrackingNumber, String propertyToSort, Sort.Direction direction) {
        Order order = orderService.findByTrackingNumber(orderTrackingNumber);
        return offerService.findAllByOrder(order, propertyToSort, direction);
    }

    @Override
    public void changeOrderStatusTOStartedByCustomer(String orderTrackingNumber) {
        orderService.changeStatusToStarted(orderTrackingNumber);
    }

    @Override
    public void changeOrderStatusTOFinishedByCustomer(String orderTrackingNumber) {
        orderService.changeStatusToFinished(orderTrackingNumber);
    }

    @Override
    public void confirmOffer(OfferId offerId) {
        Offer offer = offerService.findById(offerId);
        offerService.updateOfferAfterConfirmed(offer);
        orderService.updateOrderAfterOfferConfirmed(offer.getOfferId().getOrder(), offer.getOfferId().getExpert());
    }
}
