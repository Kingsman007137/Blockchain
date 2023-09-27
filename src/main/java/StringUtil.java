import java.security.*;
import java.util.*;

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

    /** All you really need to know is : applyECDSASig takes in the senders private key
     * and string input, signs it and returns an array of bytes. verifyECDSASig takes in
     * the signature,public key and string data and returns true or false if the signature
     * is valid.getStringFromKey returns encoded string from any key.
     */
    // Applies ECDSA Signature and returns the result (as bytes).
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            // Get an ECDSA signature instance with the provider "BC" (Bouncy Castle).
            dsa = Signature.getInstance("ECDSA", "BC");
            // Initialize the signature object with the private key.
            dsa.initSign(privateKey);
            // Convert the input string to a byte array and update the signature object.
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            // Perform the signing operation and get the signature result.
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    /// Verifies a String signature.
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            // Get an ECDSA verification instance with the provider "BC" (Bouncy Castle).
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            // Initialize the verification object with the public key.
            ecdsaVerify.initVerify(publicKey);
            // Update the verification object to include the byte representation of the input data.
            ecdsaVerify.update(data.getBytes());
            // Perform signature verification and return the result (true for success, false for failure).
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    //Tacks in array of transactions and returns a merkle root.
    public static String getMerkleRoot(ArrayList<Transaction> transactions) {
        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.transactionId);
        }

        ArrayList<String> treeLayer = previousTreeLayer;
        while(count > 1) {
            treeLayer = new ArrayList<String>();
            for(int i=1; i < previousTreeLayer.size(); i++) {
                treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }
            // Update the count for the next iteration
            count = treeLayer.size();
            // Update the previous layer
            previousTreeLayer = treeLayer;
        }

        // The final Merkle Root is the last remaining value in the treeLayer list
        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";

        return merkleRoot;
    }
}
