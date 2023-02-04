package ir.maktab.finalprojectphase2.validation;

import ir.maktab.finalprojectphase2.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"@maryam.gmail.com", "maryam.gmail.com", ".maryam@gmail.com",})
    void emailWithInValidPatternMustThrowException(String emailAddress) {
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> EmailValidator.isValidateEmail(emailAddress));
        assertEquals("the email is not valid", validationException.getMessage());
    }

}