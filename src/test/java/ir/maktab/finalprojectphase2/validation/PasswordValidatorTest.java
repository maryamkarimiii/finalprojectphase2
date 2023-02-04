package ir.maktab.finalprojectphase2.validation;

import ir.maktab.finalprojectphase2.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PasswordValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"123Km", "123456MK45", "12345678","maryamKa","mar 1234"})
    void passwordWithInValidPatternMustThrowException(String password) {
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> PasswordValidator.isValidatePassword(password));
        assertEquals("the password is not valid", validationException.getMessage());
    }

}