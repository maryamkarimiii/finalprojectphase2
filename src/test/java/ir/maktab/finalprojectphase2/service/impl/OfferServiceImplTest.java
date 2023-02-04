package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.model.Offer;
import ir.maktab.finalprojectphase2.data.model.OfferId;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.exception.ValidationException;
import ir.maktab.finalprojectphase2.service.ExpertService;
import ir.maktab.finalprojectphase2.service.OfferService;
import ir.maktab.finalprojectphase2.service.OrderService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OfferServiceImplTest {
    @Autowired
    private ExpertService expertService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private OrderService orderService;
    private Offer testObject;
    private OfferId offerId;
    private ir.maktab.finalprojectphase2.data.model.Order order;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("offerServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestObject() {
        order = orderService.findByTrackingNumber("123456");
        offerId = OfferId.builder()
                .order(order)
                .expert(expertService.findActiveExpertByUsername("maryam12@gmail.com"))
                .build();
        testObject = Offer.builder()
                .offerId(offerId)
                .price(1700D)
                .workDate(LocalDate.now())
                .duration(Duration.ofHours(2))
                .confirmedByCustomer(false)
                .build();
    }


    @Test
    @Order(1)
    void saveOfferByUnValidPriceMustThrowException() {
        testObject.setPrice(0.0);
        assertThrows(ValidationException.class, () -> offerService.save(testObject));
    }

    @Test
    @Order(2)
    void saveOfferByUnValidWorkDateMustThrowException() {
        testObject.setWorkDate(LocalDate.of(2023, 1, 1));
        assertThrows(ValidationException.class, () -> offerService.save(testObject));
    }

    @Test
    @Order(3)
    void saveOrder() {
        offerService.save(testObject);
        boolean exist = offerService.isExistByOrder(order);
        assertTrue(exist);
    }

    @Test
    @Order(4)
    void isExistByOfferId() {
        boolean exist = offerService.isExistByOfferId(testObject.getOfferId());
        assertTrue(exist);
    }

    @Test
    @Order(5)
    void findOfferByOfferId() {
        Offer offer = offerService.findById(offerId);
        System.out.println(offer);
        assertNotNull(offer);
    }


    @ParameterizedTest
    @ValueSource(strings = {"workDate", "duration"})
    @Order(6)
    void findAllByOrderWithUnValidSortPropertiesMustThrowException(String properties) {
        assertThrows(ValidationException.class,
                () -> offerService.findAllByOrder(order, properties, Sort.Direction.ASC));
    }

    @ParameterizedTest
    @ValueSource(strings = {"offerId.expert.totalScore", "price"})
    @Order(7)
    void findAllByOrder(String properties) {
        List<Offer> offerList = offerService.findAllByOrder(order, properties, Sort.Direction.ASC);
        assertThat(offerList).isNotEmpty();
    }

    @Test
    @Order(8)
    void isExistByOrder() {
        boolean exist = offerService.isExistByOrder(order);
        assertTrue(exist);
    }

    @Test
    @Order(9)
    void findOfferByOrderBeforeConfirmOfferMustThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> offerService.findByOrderAndConfirmed(order));
    }

    @Test
    @Order(10)
    void confirmOffer() {
        offerService.updateOfferAfterConfirmed(testObject);
        Offer offer = offerService.findByOrderAndConfirmed(order);
        assertTrue(offer.isConfirmedByCustomer());
    }

    @Test
    @Order(11)
    void findOfferAfterConfirmByOrder() {
        Offer offer = offerService.findByOrderAndConfirmed(order);
        assertThat(offer).isNotNull();
    }

}