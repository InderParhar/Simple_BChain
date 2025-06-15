
import java.security.MessageDigest;

public class StringUtil {

    public static String applySHA256(String input) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hex_string = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) 
                    hex_string.append('0');
                    hex_string.append(hex);
                

            }
            return hex_string.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
