import java.security.MessageDigest;

public class StringUtil {
    //Applies Sha256 to a string and returns the result.
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input by converting the input string to bytes and hashing it.
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            // This will contain hash as hexadecimal
            StringBuffer hexString = new StringBuffer();
            // Convert each byte of the hash to a two-character hexadecimal representation.
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                // If the hexadecimal representation is only one character, append a leading '0'.
                if (hex.length() == 1)
                    hexString.append('0');
                // Append the hexadecimal value to the hexString.
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
