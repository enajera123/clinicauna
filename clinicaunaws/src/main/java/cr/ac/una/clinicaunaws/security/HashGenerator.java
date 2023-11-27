package cr.ac.una.clinicaunaws.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author arayaroma
 */
public class HashGenerator {

    public enum HashAlgorithm {
        SHA256("SHA-256"),
        SHA512("SHA-512"),
        MD5("MD5");

        private final String algorithm;

        HashAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }

    public static String generateHash(String text, String algorithm) {
        try {
            MessageDigest digest;
            digest = MessageDigest.getInstance(algorithm);

            // Convert the message to bytes
            byte[] messageBytes = text.getBytes();

            // Update the digest with the message bytes
            digest.update(messageBytes);

            // Calculate the hash as a byte array
            byte[] hashBytes = digest.digest();

            // Convert the hash bytes to a hexadecimal
            StringBuilder hexText = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xFF & hashByte);
                if (hex.length() == 1) {
                    hexText.append('0');
                }
                hexText.append(hex);
            }
            return hexText.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static Boolean validateHash(String first, String second) {
        return first.equals(second);
    }
}
