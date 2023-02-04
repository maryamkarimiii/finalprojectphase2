package ir.maktab.finalprojectphase2.data.dao;

import ir.maktab.finalprojectphase2.data.enums.OrderStatus;
import ir.maktab.finalprojectphase2.data.model.Customer;
import ir.maktab.finalprojectphase2.data.model.Order;
import ir.maktab.finalprojectphase2.data.model.SubService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends JpaRepository<Order, Long> {
    List<Order>
    findAllBySubServiceInAndOrderStatusInAndDisableFalse(List<SubService> subServiceList, List<OrderStatus> orderStatusList);

    Optional<Order> findByTrackingNumberAndDisable(String trackingNumber, boolean disable);

    List<Order> findAllByCustomer(Customer customer);
}
