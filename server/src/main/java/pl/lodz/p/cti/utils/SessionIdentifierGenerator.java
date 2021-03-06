package pl.lodz.p.cti.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.math.BigInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessionIdentifierGenerator {

    private static SecureRandom random = new SecureRandom();

    public static String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}