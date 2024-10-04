package org.LinkedOn.server.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Hash {
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword.trim().replace("/n", ""));
    }
}


