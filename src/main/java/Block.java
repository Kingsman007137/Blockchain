import java.util.ArrayList;
import java.util.Date;

public class Block {

    private String ger;
    private String previousHash;
    private long timeStamp; //just like cs61b
    private int nonce;
    private String merkleRoot;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();//our data will be a simple message.

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.ger = calculateHash(); //we should do this after we set the other values
    }

    public String calculateHash() {
        String hash = StringUtil.applySha256(previousHash
                                            + merkleRoot
                                            + Long.toString(timeStamp)
                                            + Integer.toString(nonce));
        return hash;
    }

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        //create a string contains difficulty * '0'.
        String target = new String(new char[difficulty]).replace('\0', '0');
        //loop until the hash of the Block match the requirement of target.
        while (!ger.substring(0, difficulty).equals(target)) {
            nonce ++;
            ger = calculateHash();
        }
        System.out.println("Block mined!!!" + getHash());
    }

    public String getHash() {
        return ger;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((!previousHash.equals("0"))) {
            if((!transaction.processTransaction())) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}
