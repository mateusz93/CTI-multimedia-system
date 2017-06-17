package pl.lodz.p.cti.utils;

import java.security.SecureRandom;
import java.math.BigInteger;

public final class SessionIdentifierGenerator {
    private static SecureRandom random = new SecureRandom();

    public static String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}