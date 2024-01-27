package fr.openrunning.orbackend.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.openrunning.model.database.user.User;
import fr.openrunning.orbackend.common.exception.OpenRunningException;

@Component
public class SecurityEncoder {
    private static Logger logger = LoggerFactory.getLogger(SecurityEncoder.class);

    private final String ALGORITHM = "AES";
    private final String SEPARATOR = "#";

    private String signingKey;
    private String salt;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private SecretKeySpec secretKey = null;
    private byte[] key;

    public SecurityEncoder(@Value("${security.key}") String signinKey, @Value("${security.salt}") String salt) {
        this.signingKey = signinKey;
        this.salt = salt;
    }

    public String generateToken(User userInformation) throws OpenRunningException {
        try {
            LocalDateTime oneDayLater = LocalDateTime.now().plusDays(1);
            return encrypt(
                    dateFormatter.format(oneDayLater) + SEPARATOR + userInformation.getEmail() + SEPARATOR + salt);
        } catch (Exception e) {
            logger.error("error while generating token", e);
            throw buildOpenRunningExceptionForInvalidToken();
        }
    }

    public String generateEncodedPassword(String plainPassword) throws OpenRunningException {
        try {
            return encrypt(salt + SEPARATOR + plainPassword);
        } catch (Exception e) {
            logger.error("error while generating password", e);
            throw new OpenRunningException("error while generating password", e);
        }
    }

    private String encrypt(String strToEncrypt) throws OpenRunningException {
        try {
            if (secretKey == null) {
                prepareSecreteKey();
            }
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new OpenRunningException("error while encrypting", e);
        }
    }

    public void checkToken(String userToken) throws OpenRunningException {
        try {
            String plainValue = decrypt(userToken);
            String date = plainValue.split(SEPARATOR)[0];
            LocalDateTime expirationDate = LocalDateTime.parse(date, dateFormatter);
            if (LocalDateTime.now().isAfter(expirationDate)) {
                logger.warn("error while checking token: expired token");
                throw buildOpenRunningExceptionForInvalidToken();
            }
        } catch (Exception e) {
            logger.error("error while checking token", e);
            throw buildOpenRunningExceptionForInvalidToken();
        }
    }

    private String decrypt(String valueToDecrypt) throws OpenRunningException {
        try {
            if (secretKey == null) {
                prepareSecreteKey();
            }
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(valueToDecrypt)));
        } catch (Exception e) {
            throw new OpenRunningException("error while decrypting", e);
        }
    }

    private void prepareSecreteKey() throws OpenRunningException {
        MessageDigest sha = null;
        try {
            key = signingKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new OpenRunningException("error while preparing the encryption key", e);
        }
    }

    public String hashWithSHA256(String toHash) throws OpenRunningException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new OpenRunningException("error while hashing", e);
        }
    }

    protected OpenRunningException buildOpenRunningExceptionForInvalidToken() {
        return new OpenRunningException("error while checking token: invalid token");
    }
}
