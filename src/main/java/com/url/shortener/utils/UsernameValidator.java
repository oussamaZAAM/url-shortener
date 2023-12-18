package com.url.shortener.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator {

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    private static final Pattern pattern = Pattern.compile(USERNAME_REGEX);

    public static boolean isValidUsername(String username) {
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}