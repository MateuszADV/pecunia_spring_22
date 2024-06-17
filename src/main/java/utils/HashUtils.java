package utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HashUtils {
    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Boolean veryfiPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }
}
