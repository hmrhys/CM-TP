package edu.ncsu.csc.CoffeeMaker.config;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

    private static final int TOKEN_LENGTH = 64; // Length of the token in bytes

    public static String generateToken () {
        final SecureRandom secureRandom = new SecureRandom();
        final byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes( tokenBytes );
        return Base64.getEncoder().encodeToString( tokenBytes );
    }
}
