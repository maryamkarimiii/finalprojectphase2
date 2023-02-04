package ir.maktab.finalprojectphase2.validation;


import ir.maktab.finalprojectphase2.exception.ValidationException;

import java.util.regex.Pattern;

public class PhoneNumberValidator {

    private PhoneNumberValidator() {
    }

    public static boolean isValidatePhoneNumber(String phoneNumber) {
        String regex = "^09[0|123]\\d{8}$";
        if (!Pattern.matches(regex, phoneNumber))
            throw new ValidationException("the phone number is not valid");
        return true;
    }
}
