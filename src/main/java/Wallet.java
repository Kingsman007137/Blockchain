import java.security.*;
import java.security.spec.*;

public class Wallet {
    public PublicKey publicKey;
    public PrivateKey privateKey;

    public Wallet(){
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            // Create an ECDSA key pair generator, specifying the algorithm as "ECDSA" and the provider as "BC" (Bouncy Castle).
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            // Create a secure random number generator used for generating random numbers for the key pair.
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // Specify the elliptic curve parameter specification (ECGenParameterSpec), using the "prime192v1" curve here.
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key pair generator and generate a key pair.
            keyGen.initialize(ecSpec, random);   // 256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
            // Get the private and public keys from the generated key pair.
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


}
