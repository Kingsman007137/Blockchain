import java.security.*;
import java.security.spec.*;
import java.util.*;

public class Wallet {
    public PublicKey publicKey;
    public PrivateKey privateKey;
    //only UTXOs owned by this wallet.
    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

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

    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            //if output belongs to me ( if coins belong to me )
            if(UTXO.isMine(publicKey)) {
                UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
                total += UTXO.value ;
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(PublicKey recipient,float value ) {
        //gather balance and check funds.
        if(getBalance() < value) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        //create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            UTXOs.remove(input.getTransactionOutputId());
        }
        return newTransaction;
    }
}
