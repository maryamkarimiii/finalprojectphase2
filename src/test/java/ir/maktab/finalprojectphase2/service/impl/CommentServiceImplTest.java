package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.model.Comment;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.service.CommentService;
import ir.maktab.finalprojectphase2.service.CustomerService;
import ir.maktab.finalprojectphase2.service.ExpertService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceImplTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ExpertService expertService;
    private Comment testObject;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("commentServiceData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestObject() {
        testObject = Comment.builder()
                .customer(customerService.findByUsername("maryam12@gmail.com"))
                .expert(expertService.findActiveExpertByUsername("maryam12@gmail.com"))
                .customerComment("none")
                .score(4)
                .build();
    }

    @Test
    @Order(1)
    void saveComment() {
        commentService.save(testObject);
        Comment comment = commentService.findById(1L);
        assertThat(comment).isNotNull();
    }

    @Test
    @Order(2)
    void findCommentById() {
        Comment comment = commentService.findById(1L);
        assertThat(comment).isNotNull();
    }

    @Test
    @Order(3)
    void calculateAverageOfExpertScore() {
        Expert expert = testObject.getExpert();
        Double totalScore = expert.getTotalScore();
        Double averageOfExpertScore = commentService.averageOfExpertScore(expert);
        assertNotEquals(totalScore, averageOfExpertScore);
    }

    @Test
    @Order(4)
    void updateComment() {
        Comment comment = commentService.findById(1L);
        String customerComment = comment.getCustomerComment();
        comment.setCustomerComment("newComment");

        commentService.update(comment);

        Comment updatedComment = commentService.findById(1L);
        assertNotEquals(customerComment, updatedComment.getCustomerComment());
    }


}
