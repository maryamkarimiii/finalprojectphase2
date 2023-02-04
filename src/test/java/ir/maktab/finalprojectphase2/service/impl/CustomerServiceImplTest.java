package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dto.ChangePasswordDTO;
import ir.maktab.finalprojectphase2.data.enums.OrderStatus;
import ir.maktab.finalprojectphase2.data.model.*;
import ir.maktab.finalprojectphase2.exception.NotCorrectInputException;
import ir.maktab.finalprojectphase2.exception.ValidationException;
import ir.maktab.finalprojectphase2.service.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static ir.maktab.finalprojectphase2.data.enums.OrderStatus.WAITING_FOR_CUSTOMER_CHOICE;
import static ir.maktab.finalprojectphase2.data.enums.OrderStatus.WAITING_FOR_OFFER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceImplTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private SubServiceService subServiceService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ExpertService expertService;
    @Autowired
    private OfferService offerService;
    private Customer testObject;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("customerServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestObject() {
        testObject = Customer.builder()
                .firstName("maryam")
                .lastName("karimi")
                .phoneNumber("09100321487")
                .email("maryam@gmail.com")
                .username("maryam@gmail.com")
                .password("12345Mm")
                .build();
    }

    @Test
    @Order(1)
    void saveCustomer() {
        customerService.save(testObject);
        Customer customer = customerService.findByUsername(testObject.getUsername());
        assertThat(customer).isNotNull();
    }

    @Test
    @Order(2)
    void findCustomerByUsername() {
        Customer customer = customerService.findByUsername(testObject.getUsername());
        assertNotNull(customer);
    }

    @Test
    @Order(3)
    void findAllCustomer() {
        List<Customer> allCustomer = customerService.findAllCustomer();
        assertThat(allCustomer).isNotEmpty();
    }

    @Test
    @Order(4)
    void customerLogInWithInCorrectPasswordMustThrowException() {
        String username = testObject.getUsername();
        assertThrows(NotCorrectInputException.class,
                () -> customerService.login(username, "wrong password"));
    }

    @Test
    @Order(5)
    void changePasswordWithInCorrectOldPasswordMustThrowException() {
        ChangePasswordDTO changePasswordDTO =
                ChangePasswordDTO.builder()
                        .oldPassword("inCorrect input")
                        .newPassword("123")
                        .confirmPassword("123")
                        .build();

        String username = testObject.getUsername();
        assertThrows(NotCorrectInputException.class,
                () -> customerService.changePassword(changePasswordDTO, username));
    }

    @Test
    @Order(6)
    void expertLogIn() {
        Customer customer = customerService.login(testObject.getUsername(), testObject.getPassword());
        assertThat(customer).isNotNull();
    }

    @Test
    @Order(7)
    void changePasswordWithNotEqualNewAndConfirmedPasswordMustThrowException() {
        ChangePasswordDTO changePasswordDTO =
                ChangePasswordDTO.builder()
                        .oldPassword(testObject.getPassword()).
                        newPassword("123").
                        confirmPassword("1234")
                        .build();

        String username = testObject.getUsername();
        assertThrows(NotCorrectInputException.class,
                () -> customerService.changePassword(changePasswordDTO, username));
    }

    @Test
    @Order(8)
    void changeCustomerPassword() {
        ChangePasswordDTO changePasswordDTO =
                ChangePasswordDTO.builder()
                        .oldPassword(testObject.getPassword()).
                        newPassword("123").
                        confirmPassword("123")
                        .build();

        String username = testObject.getUsername();
        customerService.changePassword(changePasswordDTO, username);

        Customer customer = customerService.findByUsername(username);
        assertEquals(changePasswordDTO.getNewPassword(), customer.getPassword());
    }

    @Test
    @Order(9)
    void findAndSeeServiceToChose() {
        List<Service> serviceList = customerService.seeServicesToChose();
        assertThat(serviceList).isNotEmpty();
    }

    @Test
    @Order(10)
    void findAndSeeSubServiceToChose() {
        String serviceName = serviceService.findEnableServiceByName("service1").getName();
        List<SubService> subServiceList = customerService.seeSubServicesToChose(serviceName);
        assertThat(subServiceList).isNotEmpty();
    }

    @Test
    @Order(11)
    void createOrder() {
        ir.maktab.finalprojectphase2.data.model.Order order = buildOrder();
        customerService.createOrder(order);

        ir.maktab.finalprojectphase2.data.model.Order customerOrder
                = orderService.findByTrackingNumber(order.getTrackingNumber());

        assertThat(customerOrder).isNotNull();
        assertEquals(testObject.getUsername(), customerOrder.getCustomer().getUsername());
    }

    @Test
    @Order(12)
    void seeOrderOffers() {
        saveOffer();
        List<Offer> offerList =
                customerService.seeOrderOffers("123456", "price", Sort.Direction.ASC);
        assertThat(offerList).isNotEmpty();
    }

    @Test
    @Order(13)
    void confirmOfferByCustomer() {
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber("123456");
        orderService.changeStatusToWaitingForCustomerChose(order);
        OfferId offerId = OfferId.builder()
                .order(order)
                .expert(expertService.findActiveExpertByUsername("maryam12@gmail.com"))
                .build();

        customerService.confirmOffer(offerId);
        boolean confirmedByCustomer = offerService.findById(offerId).isConfirmedByCustomer();
        ir.maktab.finalprojectphase2.data.model.Order orderAfterConfirm = orderService.findByTrackingNumber("123456");

        assertTrue(confirmedByCustomer);
        assertThat(orderAfterConfirm.getExpert()).isNotNull();
    }

    @Test
    @Order(14)
    void changerOrderStatusToStarting() {
        customerService.changeOrderStatusTOStartedByCustomer("123456");

        ir.maktab.finalprojectphase2.data.model.Order orderAfterUpdate = orderService.findByTrackingNumber("123456");

        assertEquals(OrderStatus.START_WORKING, orderAfterUpdate.getOrderStatus());
    }

    @Test
    @Order(15)
    void changerOrderStatusToFinishing() {
        customerService.changeOrderStatusTOFinishedByCustomer("123456");

        ir.maktab.finalprojectphase2.data.model.Order orderAfterUpdate = orderService.findByTrackingNumber("123456");

        assertEquals(OrderStatus.FINISH_WORKING, orderAfterUpdate.getOrderStatus());
    }


    private ir.maktab.finalprojectphase2.data.model.Order buildOrder() {
        return ir.maktab.finalprojectphase2.data.model.Order.builder()
                .trackingNumber("123456")
                .customer(customerService.findByUsername(testObject.getUsername()))
                .subService(subServiceService.findEnableSubServiceByName("subService1"))
                .description("none")
                .price(1700D)
                .workDate(LocalDate.now())
                .address("none")
                .orderStatus(WAITING_FOR_OFFER)
                .build();
    }

    private void saveOffer() {
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber("123456");
        OfferId offerId = OfferId.builder()
                .order(order)
                .expert(expertService.findActiveExpertByUsername("maryam12@gmail.com"))
                .build();
        Offer offer = Offer.builder()
                .offerId(offerId)
                .price(1700D)
                .workDate(LocalDate.now())
                .duration(Duration.ofHours(2))
                .confirmedByCustomer(false)
                .build();
        offerService.save(offer);
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

}