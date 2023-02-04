package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.model.Service;
import ir.maktab.finalprojectphase2.service.ServiceService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServiceServiceImplTest {
    @Autowired
    private ServiceService serviceService;
    private static Service testObject;

    @BeforeAll
    static void initializeTestObject() {
        testObject = Service.builder()
                .name("service1")
                .build();
    }

    @Test
    @Order(1)
    void saveService() {
        serviceService.save(testObject);
        Service service = serviceService.findEnableServiceByName(testObject.getName());
        assertNotNull(service);
    }

    @Test
    @Order(2)
    void isExistService() {
        boolean exist = serviceService.isExist(testObject.getName());
        assertTrue(exist);
    }

    @Test
    @Order(3)
    void findEnableServiceByName() {
        Service service = serviceService.findEnableServiceByName(testObject.getName());
        assertNotNull(service);
    }

    @Test
    @Order(4)
    void findAllEnableService() {
        List<Service> serviceList = serviceService.findAllEnableService();
        assertThat(serviceList).isNotEmpty();
    }

    @Test
    @Order(5)
    void softDeleteService() {
        Service service = serviceService.findEnableServiceByName(testObject.getName());
        serviceService.softDelete(service);
        Service serviceAfterDeleted = serviceService.findDisableServiceByName(testObject.getName());
        assertTrue(serviceAfterDeleted.isDisable());
    }

    @Test
    @Order(6)
    void findDisableServiceByName() {
        Service disableService = serviceService.findDisableServiceByName(testObject.getName());
        assertNotNull(disableService);
    }

    @Test
    @Order(7)
    void findAllDisableService() {
        List<Service> serviceList = serviceService.findAllDisableService();
        assertThat(serviceList).isNotEmpty();
    }


    @Test
    @Order(8)
    void activeDisableService() {
        serviceService.activeDisableService(testObject.getName());
        Service service = serviceService.findEnableServiceByName(testObject.getName());
        assertFalse(service.isDisable());
    }

}