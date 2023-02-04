package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.enums.Role;
import ir.maktab.finalprojectphase2.data.model.Admin;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Service;
import ir.maktab.finalprojectphase2.data.model.SubService;
import ir.maktab.finalprojectphase2.exception.DuplicationException;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.service.AdminService;
import ir.maktab.finalprojectphase2.service.ServiceService;
import ir.maktab.finalprojectphase2.service.SubServiceService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminServiceImplTest {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private SubServiceService subServiceService;
    private Admin testObject;
    private final Service testService =
            Service.builder().id(1L).name("service1").build();
    private final SubService testSubService =
            SubService.builder().id(1L).service(testService).name("sub1").description("none").baseAmount(1.2).build();

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("adminServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestObject() {
        testObject = Admin.builder()
                .fullName("maryam")
                .username("maryam@gmail.com")
                .password("12345Mm")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    @Order(1)
    void saveAdmin() {
        adminService.save(testObject);
        boolean exist = adminService.isExistByUsername(testObject.getUsername());
        assertTrue(exist);
    }

    @Test
    @Order(2)
    void addNewService() {
        adminService.addNewService(testService);

        Service service = serviceService.findEnableServiceByName(testService.getName());
        assertNotNull(service);
    }

    @Test
    @Order(3)
    void addNewServiceWhenServiceExistMustThrowException() {
        assertThrows(DuplicationException.class, () -> adminService.addNewService(testService));
    }

    @Test
    @Order(4)
    void seeAllEnableService() {
        List<Service> serviceList = adminService.seeALLEnableService();
        assertThat(serviceList).isNotEmpty();
    }

    @Test
    @Order(5)
    void addNewSubService() {
        adminService.addNewSubService(testSubService);

        SubService subService = subServiceService.findEnableSubServiceByName(testSubService.getName());
        assertNotNull(subService);
    }

    @Test
    @Order(6)
    void addNewSubServiceWhenSubServiceExistMustThrowException() {
        assertThrows(DuplicationException.class, () -> adminService.addNewSubService(testSubService));
    }

    @Test
    @Order(7)
    void seeAllEnableSubService() {
        Map<Service, List<SubService>> serviceListMap = adminService.seeALLEnableSubService();
        assertThat(serviceListMap).isNotEmpty();
    }

    @Test
    @Order(8)
    void disableActiveSubService() {
        adminService.deleteExistenceSubService(testSubService);

        Map<Service, List<SubService>> serviceListMap = adminService.seeALLEnableSubService();
        assertThat(serviceListMap).isEmpty();
    }

    @Test
    @Order(9)
    void seeAllDisableSubService() {
        Map<Service, List<SubService>> serviceListMap = adminService.seeALLDisableSubService();
        assertThat(serviceListMap).isNotEmpty();
    }

    @Test
    @Order(10)
    void disableActiveService() {
        adminService.deleteExistenceService(testService);

        List<Service> serviceList = adminService.seeALLEnableService();
        assertThat(serviceList).isEmpty();
    }

    @Test
    @Order(11)
    void seeAllDisableService() {
        List<Service> serviceList = adminService.seeAllDisableService();
        assertThat(serviceList).isNotEmpty();
    }

    @Test
    @Order(12)
    void activeDisableService() {
        String serviceName = testService.getName();
        adminService.activeDisableService(serviceName);

        assertThrows(NotFoundException.class, () -> adminService.findDisableServiceByName(serviceName));
    }

    @Test
    @Order(13)
    void activeDisableSubService() {
        String subServiceName = testSubService.getName();
        adminService.activeDisableSubService(testSubService.getName());

        assertThrows(NotFoundException.class, () -> adminService.findDisableSubServiceByName(subServiceName));
    }

    @Test
    @Order(14)
    void seeAllExpertWithNewRegistrationStatus() {
        List<Expert> experts = adminService.seeAllExpertWithNewExpertRegistrationStatus();
        assertThat(experts).isNotEmpty();
    }

    @Test
    @Order(15)
    void confirmExpertByAdmin() {
        adminService.confirmExpert("maryam12@gmail.com");

        List<Expert> experts = adminService.seeAllExpertWithNewExpertRegistrationStatus();
        System.out.println(experts);
        assertThat(experts).isEmpty();
    }

    @Test
    @Order(16)
    void updateSubServicePrice() {
        adminService.updateSubServicePrice(testSubService.getName(), 2.2);
        SubService subServiceAfterUpdate = subServiceService.findEnableSubServiceByName(testSubService.getName());

        assertNotEquals(testSubService.getBaseAmount(), subServiceAfterUpdate.getBaseAmount());
    }

    @Test
    @Order(16)
    void AddExpertToSubService() {
        String subServiceName = testSubService.getName();
        adminService.addExpertToSubService("maryam12@gmail.com", subServiceName);

        List<Expert> expertList = subServiceService.findSubServiceExpertsBySubServiceName(subServiceName);
        assertThat(expertList).isNotEmpty();
    }

    @Test
    @Order(17)
    void deleteExpertFromSubService() {
        String subServiceName = testSubService.getName();
        adminService.deleteExpertFromSubService("maryam12@gmail.com", subServiceName);

        List<Expert> expertList = subServiceService.findSubServiceExpertsBySubServiceName(subServiceName);
        assertThat(expertList).isEmpty();
    }

    @Test
    @Order(18)
    void updateSubServiceDescription() {
        adminService.updateSubServiceDescription(testSubService.getName(), "new");
        SubService subServiceAfterUpdate = subServiceService.findEnableSubServiceByName(testSubService.getName());

        assertNotEquals(testSubService.getDescription(), subServiceAfterUpdate.getDescription());
    }

}