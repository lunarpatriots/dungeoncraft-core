package com.lunarpatriots.dungeoncraft.common.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class HashingUtil {

  private static final Random RANDOM = new SecureRandom();

  public static String sha256(String input) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes());

      StringBuilder hex = new StringBuilder();
      for (byte b : hash) {
        hex.append(String.format("%02x", b));
      }
      return hex.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String generateNonce(final int byteLength) {
    byte[] bytes = new byte[byteLength];
    RANDOM.nextBytes(bytes);

    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private HashingUtil() {
  }
}
