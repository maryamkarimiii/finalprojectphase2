package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dto.ChangePasswordDTO;
import ir.maktab.finalprojectphase2.data.enums.ExpertRegistrationStatus;
import ir.maktab.finalprojectphase2.data.model.Comment;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.exception.NotCorrectInputException;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.service.CommentService;
import ir.maktab.finalprojectphase2.service.ExpertService;
import ir.maktab.finalprojectphase2.service.OrderService;
import ir.maktab.finalprojectphase2.service.SubServiceService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExpertServiceImplTest {
    @Autowired
    private ExpertService expertService;
    @Autowired
    private SubServiceService subServiceService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private OrderService orderService;
    private Expert testObject;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("expertServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestObject() {
        testObject = Expert.builder()
                .firstName("maryam")
                .lastName("karimi")
                .phoneNumber("09100321487")
                .email("maryam@gmail.com")
                .username("maryam@gmail.com")
                .password("12345Mm")
                .subServiceSet(Set.of(subServiceService.findEnableSubServiceByName("subService1")))
                .build();
    }

    @Test
    @Order(1)
    void saveExpert() {
        expertService.save(testObject);
        boolean exist = expertService.isExist(testObject.getUsername());
        assertTrue(exist);
    }

    @Test
    @Order(2)
    void setExpertImage() throws IOException {
        String filePath = "D:\\intllij\\finalprojectphase2\\src\\test\\resources\\staticImage\\water-drop-300KB-2.jpg";
        Expert expertWithNullImage = expertService.findActiveExpertByUsername(testObject.getUsername());
        expertService.setExpertImage(expertWithNullImage, filePath);

        Expert expertAfterSetImage = expertService.findActiveExpertByUsername(testObject.getUsername());
        assertThat(expertAfterSetImage.getImage()).isNotNull();
    }

    @Test
    @Order(3)
    void getExpertImage() throws IOException {
        String filePath = "D:\\intllij\\finalprojectphase2\\src\\test\\resources\\staticImage\\1.jpg";
        expertService.getExpertImage(testObject.getUsername(), filePath);
        Path path = Path.of(filePath);
        boolean exists = Files.exists(path);
        assertTrue(exists);
    }

    @Test
    @Order(4)
    void isExistExpertByUsername() {
        boolean exist = expertService.isExist(testObject.getUsername());
        assertTrue(exist);
    }

    @Test
    @Order(5)
    void findActiveExpertByUsername() {
        Expert expert = expertService.findActiveExpertByUsername(testObject.getUsername());
        assertNotNull(expert);
    }

    @Test
    @Order(6)
    void findDeActiveExpertByUsernameWhenExpertIsActiveMustThrowsException() {
        String username = testObject.getUsername();
        assertThrows(NotFoundException.class, () -> expertService.findDeActiveExpertByUsername(username));
    }

    @Test
    @Order(7)
    void findAllByExpertRegistrationStatusNew() {
        List<Expert> expertList = expertService.findAllWithNewExpertRegistrationStatus();
        assertThat(expertList).isNotEmpty();
    }

    @Test
    @Order(8)
    void expertLogInWithInCorrectPasswordMustThrowException() {
        String username = testObject.getUsername();
        assertThrows(NotCorrectInputException.class,
                () -> expertService.login(username, "wrong password"));
    }

    @Test
    @Order(9)
    void expertLogInWithNewExpertRegistrationStatusMustThrowException() {
        String username = testObject.getUsername();
        String password = testObject.getPassword();
        assertThrows(NotCorrectInputException.class,
                () -> expertService.login(username, password));
    }

    @Test
    @Order(10)
    void changeExpertStatusToConfirm() {
        Expert expert = expertService.findActiveExpertByUsername(testObject.getUsername());
        expert.setExpertRegistrationStatus(ExpertRegistrationStatus.CONFIRMED);
        expertService.update(expert);

        Expert expertAfterUpdate = expertService.findActiveExpertByUsername(testObject.getUsername());
        assertEquals(ExpertRegistrationStatus.CONFIRMED, expertAfterUpdate.getExpertRegistrationStatus());
    }

    @Test
    @Order(11)
    void expertLogIn() {
        Expert expert = expertService.login(testObject.getUsername(), testObject.getPassword());
        assertThat(expert).isNotNull();
    }

    @Test
    @Order(12)
    void findAllByNewExpertRegistrationStatusWhenTheExpertIsConfirmedMustBeEmpty() {
        List<Expert> expertList = expertService.findAllWithNewExpertRegistrationStatus();
        assertThat(expertList).isEmpty();
    }

    @Test
    @Order(13)
    void changePasswordWithInCorrectOldPasswordMustThrowException() {
        ChangePasswordDTO changePasswordDTO =
                ChangePasswordDTO.builder()
                        .oldPassword("inCorrect input")
                        .newPassword("123")
                        .confirmPassword("123")
                        .build();

        String username = testObject.getUsername();
        assertThrows(NotCorrectInputException.class,
                () -> expertService.changePassword(changePasswordDTO, username));
    }

    @Test
    @Order(14)
    void changePasswordWithNotEqualNewAndConfirmedPasswordMustThrowException() {
        ChangePasswordDTO changePasswordDTO =
                ChangePasswordDTO.builder()
                        .oldPassword(testObject.getPassword()).
                        newPassword("123").
                        confirmPassword("1234")
                        .build();

        String username = testObject.getUsername();
        assertThrows(NotCorrectInputException.class,
                () -> expertService.changePassword(changePasswordDTO, username));
    }

    @Test
    @Order(15)
    void changeExpertPassword() {
        ChangePasswordDTO changePasswordDTO =
                ChangePasswordDTO.builder()
                        .oldPassword(testObject.getPassword()).
                        newPassword("123").
                        confirmPassword("123")
                        .build();

        String username = testObject.getUsername();
        expertService.changePassword(changePasswordDTO, username);

        Expert expert = expertService.findActiveExpertByUsername(username);
        assertEquals(changePasswordDTO.getNewPassword(), expert.getPassword());
    }

    @Test
    @Order(16)
    void findExpertOrders() {
        setOrderExpert();
        List<ir.maktab.finalprojectphase2.data.model.Order> expertsRelatedOrders
                = expertService.findExpertsRelatedOrders(testObject.getUsername());

        assertThat(expertsRelatedOrders).isNotEmpty();
    }

    @Test
    @Order(17)
    void calculateAndUpdateExpertScore() {
        createCommentForExpert();
        Expert expertBeforeUpdateScore = expertService.findActiveExpertByUsername(testObject.getUsername());
        Double totalScore = expertBeforeUpdateScore.getTotalScore();

        expertService.calculateAndUpdateExpertScore(expertBeforeUpdateScore);

        Expert expertAfterUpdateScore = expertService.findActiveExpertByUsername(testObject.getUsername());
        assertThat(expertAfterUpdateScore.getTotalScore()).isNotEqualTo(totalScore);
    }

    @Test
    @Order(18)
    void softDeleteExpert() {
        String username = testObject.getUsername();
        Expert expert = expertService.findActiveExpertByUsername(username);
        expertService.softDelete(expert);

        assertThrows(NotFoundException.class, () -> expertService.findActiveExpertByUsername(username));
    }

    @Test
    @Order(19)
    void findDisableExpertByUsername() {
        Expert expertAfterDelete = expertService.findDeActiveExpertByUsername(testObject.getUsername());
        assertNotNull(expertAfterDelete);
    }

    private void setOrderExpert() {
        ir.maktab.finalprojectphase2.data.model.Order order = orderService.findByTrackingNumber("123456");
        order.setExpert(expertService.findActiveExpertByUsername(testObject.getUsername()));
        orderService.update(order);
    }

    private void createCommentForExpert() {
        Expert expert = expertService.findActiveExpertByUsername(testObject.getUsername());
        Comment comment = commentService.findById(1L);
        comment.setExpert(expert);
        commentService.update(comment);
    }

}