package ir.maktab.finalprojectphase2.validation;

import ir.maktab.finalprojectphase2.exception.ValidationException;

import java.util.regex.Pattern;

public class EmailValidator {

    private EmailValidator() {
    }

    public static void isValidateEmail(String email) {
        String regex = "^(?=.{1,64}@)[A-Za-z\\d_-]+(\\.[A-Za-z\\d_-]+)*@"
                + "[^-][A-Za-z\\d-]+(\\.[A-Za-z\\d-]+)*(\\.[A-Za-z]{2,})$";
        if (!Pattern.matches(regex, email))
            throw new ValidationException("the email is not valid");
    }
}
