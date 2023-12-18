package com.url.shortener.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.UUID;

@Slf4j
public class Base62Converter {
    public static String convertToBase62(long value) {
        // Define the base62 characters
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        // Convert the long value to base62
        StringBuilder base62StringBuilder = new StringBuilder();
        BigInteger bigInteger = BigInteger.valueOf(value);
        while (bigInteger.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] quotientAndRemainder = bigInteger.divideAndRemainder(BigInteger.valueOf(62));
            bigInteger = quotientAndRemainder[0];
            int remainder = quotientAndRemainder[1].intValue();
            base62StringBuilder.insert(0, characters.charAt(remainder));
        }

        return base62StringBuilder.toString();
    }
}
