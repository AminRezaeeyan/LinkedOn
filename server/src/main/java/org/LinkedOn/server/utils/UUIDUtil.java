package org.LinkedOn.server.utils;

import java.util.UUID;

public class UUIDUtil {
    // Generate a UUID string and remove hyphens
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    // Convert binary (byte array) back to UUID string
    public static String bytesToUUID(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Convert UUID string back to binary (byte array)
    public static byte[] UUIDToBytes(String UUID) {
        int len = UUID.length();
        byte[] byteArray = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(UUID.charAt(i), 16) << 4)
                    + Character.digit(UUID.charAt(i + 1), 16));
        }
        return byteArray;
    }
}
