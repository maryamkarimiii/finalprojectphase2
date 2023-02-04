package ir.maktab.finalprojectphase2.validation;

import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ImageValidatorTest {

    @Test
    void validateFileExistence() {
        String filePass = "D:\\intllij\\finalprojectphase2\\src\\test\\resources\\none";
        NotFoundException notFoundException
                = assertThrows(NotFoundException.class, () -> ImageValidator.validateFileExistence(filePass));
        assertEquals("image not found", notFoundException.getMessage());
    }

    @Test
    void validateFilePass() {
        String filePass = "D:\\intllij\\finalprojectphase2\\src\\test\\resources\\staticImage\\horsepng.parspng.com-3.png";
        ValidationException validationException
                = assertThrows(ValidationException.class, () -> ImageValidator.validateFilePass(filePass));
        assertEquals("the image is not valid,image format must be jpg", validationException.getMessage());
    }

    @Test
    void validateImageSize() {
        String filePass = "D:\\intllij\\finalprojectphase2\\src\\test\\resources\\staticImage\\horsepng.parspng.com-3.png";
        ValidationException validationException
                = assertThrows(ValidationException.class, () -> ImageValidator.validateImageSize(filePass));
        assertEquals("the image is not valid,image size must be less than 300 kb", validationException.getMessage());
    }
}