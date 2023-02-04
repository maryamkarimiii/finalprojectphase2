package ir.maktab.finalprojectphase2.validation;

import ir.maktab.finalprojectphase2.exception.ValidationException;

import java.util.regex.Pattern;

public class PasswordValidator {

    private PasswordValidator() {
    }

    public static void isValidatePassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-zA-Z])(?=\\S+$).{8}$";
        if (!Pattern.matches(regex, password))
            throw new ValidationException("the password is not valid");
    }
}
