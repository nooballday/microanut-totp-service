package totp.service;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.stream.IntStream;

//credit : https://gist.github.com/rakeshopensource/def80fac825c3e65804e0d080d2fa9a7
public class TOTP {

    private static final String HMAC_ALGO = "HmacSHA1";
    private static final int TOTP_LENGTH = 6;
    private static final int TIME_STEP = 30;

    public static boolean validate(String secret, String token) {
        long timeInterval = System.currentTimeMillis() / 1000 / TIME_STEP;
        boolean matches = IntStream.of(-1, 0, 1)
                .anyMatch(i -> generateTOTP(secret, timeInterval + i).equals(token));
        if (matches) {
            return  true;
        }
        return false;
    }

    public static String generateTOTP(String secret) {
        long timeInterval = System.currentTimeMillis() / 1000 / TIME_STEP;
        return generateTOTP(secret, timeInterval);
    }

    private static String generateTOTP(String secret, long timeInterval) {
        try {
            byte[] decodedKey = decode32byte(secret);
            byte[] timeIntervalBytes = new byte[8];

            for (int i = 7; i >= 0; i--) {
                // Extract the least significant byte from timeInterval
                timeIntervalBytes[i] = (byte) (timeInterval & 0xFF);
                // Right shift to process the next byte
                timeInterval >>= 8;
            }

            Mac hmac = Mac.getInstance(HMAC_ALGO);
            hmac.init(new SecretKeySpec(decodedKey, HMAC_ALGO));
            byte[] hash = hmac.doFinal(timeIntervalBytes);

            /*
             * The line offset = hash[hash.length - 1] & 0xF; is used to determine the offset into the HMAC hash
             * from which a 4-byte dynamic binary code will be extracted to generate the TOTP.
             * This method of determining the offset is specified in the TOTP (RFC 6238) and HOTP (RFC 4226) standards.
             */
            int offset = hash[hash.length - 1] & 0xF;

            /*
             * The expression hash[offset] & 0x7F uses the hexadecimal value 0x7F to mask
             * the most significant bit (MSB) of the byte at hash[offset],
             * ensuring it's set to 0. The reason for this is to make sure that the resulting 32-bit integer
             * (binaryCode) is treated as a positive number. Reference TOTP (RFC 6238)
             */
            long mostSignificantByte = (hash[offset] & 0x7F) << 24;
            long secondMostSignificantByte = (hash[offset + 1] & 0xFF) << 16;
            long thirdMostSignificantByte = (hash[offset + 2] & 0xFF) << 8;
            long leastSignificantByte = hash[offset + 3] & 0xFF;

            long binaryCode = mostSignificantByte
                    | secondMostSignificantByte
                    | thirdMostSignificantByte
                    | leastSignificantByte;

            int totp = (int) (binaryCode % Math.pow(10, TOTP_LENGTH));
            return String.format("%0" + TOTP_LENGTH + "d", totp); // Making sure length is equal to TOTP_LENGTH
        } catch (Exception e) {
            return null;
        }
    }

    //it has to comply with RFC 3548
    private static byte[] decode32byte(String input) {
        Base32 base32 = new Base32();
        return base32.decode(input);
    }
}
