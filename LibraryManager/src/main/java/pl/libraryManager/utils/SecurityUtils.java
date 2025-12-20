package pl.libraryManager.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class SecurityUtils {
    public static String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
