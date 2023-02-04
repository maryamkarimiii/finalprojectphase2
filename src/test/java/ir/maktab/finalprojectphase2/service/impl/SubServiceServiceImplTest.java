package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Service;
import ir.maktab.finalprojectphase2.data.model.SubService;
import ir.maktab.finalprojectphase2.service.ExpertService;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SubServiceServiceImplTest {
    @Autowired
    private SubServiceService subServiceService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ExpertService expertService;
    private SubService testObject;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("subServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestObject() {
        Set<Expert> expertSet = Set.of(expertService.findActiveExpertByUsername("maryam1@gmail.com"),
                expertService.findActiveExpertByUsername("maryam12@gmail.com"));
        testObject = SubService.builder()
                .service(serviceService.findEnableServiceByName("service1"))
                .name("subService1")
                .baseAmount(120000D)
                .description("noThing")
                .expertSet(expertSet)
                .build();
    }


    @Test
    @Order(1)
    void saveSubService() {
        subServiceService.save(testObject);
        SubService subService = subServiceService.findEnableSubServiceByName(testObject.getName());
        assertNotNull(subService);
    }

    @Test
    @Order(2)
    void isExistSubService() {
        boolean exist = subServiceService.isExist(testObject.getName());
        assertTrue(exist);
    }

    @Test
    @Order(3)
    void findEnableSubServiceByName() {
        SubService subService = subServiceService.findEnableSubServiceByName(testObject.getName());
        assertNotNull(subService);
    }

    @Test
    @Order(4)
    void findAllEnableSubService() {
        Map<Service, List<SubService>> listMap = subServiceService.findAllEnableSubService();
        assertThat(listMap).isNotEmpty();
    }

    @Test
    @Order(5)
    void findSubServiceExperts() {
        List<Expert> expertList = subServiceService.findSubServiceExpertsBySubServiceName(testObject.getName());
        assertThat(expertList).isNotEmpty();
    }

    @Test
    @Order(6)
    void softDeleteSubService() {
        SubService subService = subServiceService.findEnableSubServiceByName(testObject.getName());
        subServiceService.softDelete(subService);
        SubService subServiceAfterDeleted = subServiceService.findDisableSubServiceByName(testObject.getName());
        assertTrue(subServiceAfterDeleted.isDisable());
    }

    @Test
    @Order(7)
    void findDisableSubServiceByName() {
        SubService disableSubService = subServiceService.findDisableSubServiceByName(testObject.getName());
        assertNotNull(disableSubService);
    }

    @Test
    @Order(8)
    void findAllDisableSubService() {
        Map<Service, List<SubService>> listMap = subServiceService.findAllDisableSubService();
        assertThat(listMap).isNotEmpty();
    }

    @Test
    @Order(9)
    void activeDisableSubService() {
        subServiceService.activeDisableSubService(testObject.getName());
        SubService subService = subServiceService.findEnableSubServiceByName(testObject.getName());
        assertFalse(subService.isDisable());
    }

    @Test
    @Order(10)
    void findAllByServiceName() {
        List<SubService> subServiceList = subServiceService.findAllByServiceName(testObject.getService().getName());
        assertThat(subServiceList).isNotEmpty();
    }

    @Test
    @Order(11)
    void updateSubServicePrice() {
        Double baseAmountBeforeChange = testObject.getBaseAmount();
        subServiceService.updateSubServicePrice(testObject.getName(), 100D);
        Double baseAmountAfterChange = subServiceService.findEnableSubServiceByName(testObject.getName()).getBaseAmount();
        assertThat(baseAmountBeforeChange).isNotEqualTo(baseAmountAfterChange);
    }

    @Test
    @Order(12)
    void updateSubServiceDescription() {
        String descriptionBeforeChange = testObject.getDescription();
        subServiceService.updateSubServiceDescription(testObject.getName(), "new description");
        String descriptionAfterChange = subServiceService.findEnableSubServiceByName(testObject.getName()).getDescription();
        assertThat(descriptionAfterChange).isNotEqualTo(descriptionBeforeChange);
    }

}