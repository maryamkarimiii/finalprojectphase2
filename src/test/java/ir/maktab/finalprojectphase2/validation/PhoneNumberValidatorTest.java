package ir.maktab.finalprojectphase2.validation;

import ir.maktab.finalprojectphase2.exception.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PhoneNumberValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"06100321487", "09664562565","0910032","03121234587"})
    void phoneNumberWithInValidPatternMustThrowException(String phoneNumber) {
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> PhoneNumberValidator.isValidatePhoneNumber(phoneNumber));
        assertEquals("the phone number is not valid", validationException.getMessage());
    }

}