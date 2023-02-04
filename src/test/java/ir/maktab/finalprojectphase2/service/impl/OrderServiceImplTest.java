package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Offer;
import ir.maktab.finalprojectphase2.data.model.OfferId;
import ir.maktab.finalprojectphase2.data.model.SubService;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.exception.ValidationException;
import ir.maktab.finalprojectphase2.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static ir.maktab.finalprojectphase2.data.enums.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceImplTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ExpertService expertService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private SubServiceService subServiceService;
    private Expert expert;
    private ir.maktab.finalprojectphase2.data.model.Order testObject;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("orderServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestObject() {
        testObject = ir.maktab.finalprojectphase2.data.model.Order.builder()
                .trackingNumber("123456")
                .customer(customerService.findByUsername("maryam12@gmail.com"))
                .subService(subServiceService.findEnableSubServiceByName("subService1"))
                .description("none")
                .price(1700D)
                .workDate(new Date())
                .address("none")
                .orderStatus(WAITING_FOR_OFFER)
                .build();
    }


    @Test
    @Order(1)
    void saveOrderByUnValidPriceMustThrowException() {
        testObject.setPrice(5.0);
        assertThrows(ValidationException.class, () -> orderService.save(testObject));
    }

    @Test
    @Order(2)
    void saveOrderByUnValidWorkDateMustThrowException() {
        testObject.setWorkDate(convertStringToDate("2023-01-01"));
        assertThrows(ValidationException.class, () -> orderService.save(testObject));
    }

    @Test
    @Order(3)
    void saveOrder() {
        orderService.save(testObject);
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        assertNotNull(order);
    }


    @Test
    @Order(4)
    void findEnableOrderByTrackingNumber() {
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        assertNotNull(order);
    }

    @Test
    @Order(5)
    void findAllBySubServiceAndOrderStatus() {
        List<SubService> subServiceList = List.of(testObject.getSubService());
        List<ir.maktab.finalprojectphase2.data.model.Order> orderList = orderService.findAllBySubServiceAndOrderStatus(subServiceList);
        assertThat(orderList).isNotEmpty();
    }


    @Test
    @Order(6)
    void changeStatusToStartedBeforeOfferWorkDateMustThrowException() {
        createAndSaveOffer();
        String trackingNumber = testObject.getTrackingNumber();
        assertThrows(ValidationException.class, () -> orderService.changeStatusToStarted(trackingNumber));
    }

    @Test
    @Order(7)
    void changeStatusToStartedByUnValidOrderStatusMustThrowException() {
        String trackingNumber = testObject.getTrackingNumber();
        assertThrows(ValidationException.class, () -> orderService.changeStatusToStarted(trackingNumber));
    }

    @Test
    @Order(8)
    void changeOrderStatusToWaitingForCustomerChose() {
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        order.setOrderStatus(WAITING_FOR_CUSTOMER_CHOICE);
        orderService.update(order);

        ir.maktab.finalprojectphase2.data.model.Order orderAfterUpdate = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        assertEquals(WAITING_FOR_CUSTOMER_CHOICE, orderAfterUpdate.getOrderStatus());
    }

    @Test
    @Order(9)
    void updateOrderAfterOfferConfirmed() {
        createAndSaveOffer();
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        orderService.updateOrderAfterOfferConfirmed(order, expert);

        ir.maktab.finalprojectphase2.data.model.Order orderAfterOfferConfirm
                = orderService.findByTrackingNumber(testObject.getTrackingNumber());

        assertNotNull(orderAfterOfferConfirm.getExpert());
        assertEquals(WAITING_FOR_COMING, orderAfterOfferConfirm.getOrderStatus());
    }

    @Test
    @Order(10)
    void findAllBySubServiceAndOrderStatusWhenOrderStatusIsNotWaitingForRecommendAndWaitingForCustomerChoseMustBeEmpty() {
        List<SubService> subServiceList = List.of(testObject.getSubService());
        List<ir.maktab.finalprojectphase2.data.model.Order> orderList = orderService.findAllBySubServiceAndOrderStatus(subServiceList);
        assertThat(orderList).isEmpty();
    }

    @Test
    @Order(11)
    void changeStatusToFinishBeforeStartedStatusMustThrowException() {
        String trackingNumber = testObject.getTrackingNumber();
        assertThrows(ValidationException.class, () -> orderService.changeStatusToFinished(trackingNumber));
    }

    @Test
    @Order(12)
    void changeStatusToStarted() {
        changeOfferDate();
        orderService.changeStatusToStarted(testObject.getTrackingNumber());
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        assertEquals(START_WORKING, order.getOrderStatus());
    }

    @Test
    @Order(13)
    void changeStatusToFinished() {
        orderService.changeStatusToFinished(testObject.getTrackingNumber());
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        assertEquals(FINISH_WORKING, order.getOrderStatus());
    }

    @Test
    @Order(14)
    void softDeleteOrder() {
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        orderService.softDelete(order);
        String trackingNumber = testObject.getTrackingNumber();
        assertThrows(NotFoundException.class, () -> orderService.findByTrackingNumber(trackingNumber));
    }

    private Date convertStringToDate(String stringDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            throw new ValidationException("the date format is not correct");
        }
        return date;
    }

    private void createAndSaveOffer() {
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        expert = expertService.findActiveExpertByUsername("maryam12@gmail.com");
        OfferId offerId = OfferId.builder().order(order).expert(expert).build();
        Offer offer = Offer.builder()
                .offerId(offerId)
                .price(1300D)
                .workDate(convertStringToDate("2023-08-12"))
                .duration(Duration.ofHours(2))
                .confirmedByCustomer(true)
                .build();
        offerService.save(offer);
    }

    private void changeOfferDate() {
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber(testObject.getTrackingNumber());
        Offer offer = offerService.findByOrderAndConfirmed(order);
        offer.setWorkDate(new Date());
        offerService.update(offer);
    }

}