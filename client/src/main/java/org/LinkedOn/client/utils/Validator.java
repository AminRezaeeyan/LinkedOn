package org.linkedon.client.utils;

import java.util.regex.Pattern;

public class Validator {

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String emailAddress) {
        return EMAIL_PATTERN.matcher(emailAddress).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
