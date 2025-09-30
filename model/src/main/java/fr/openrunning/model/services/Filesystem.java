package fr.openrunning.model.services;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.openrunning.model.exception.OpenRunningException;

@Service
public class Filesystem {

    private final String gpxRootDirectory;

    public Filesystem(@Value("${filesystem.rootdir}") String rootDir) {
        this.gpxRootDirectory = rootDir;
    }

    public File userUploadDirectory(String email) throws OpenRunningException {
        File uploadDirectory = new File(gpxRootDirectory);
        return new File(uploadDirectory, this.hashWithSHA256(email));
    }

    private String hashWithSHA256(String toHash) throws OpenRunningException {
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

    public boolean exists(File file) {
        return file.exists();
    }
}
