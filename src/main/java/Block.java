import java.util.Date;

public class Block {

    private String hash;
    private String previousHash;
    private String data; //our data will be a simple message.
    private long timeStamp; //just like cs61b
    private int nonce;

    public Block(String data, String previousHash) {
        this.previousHash = previousHash;
        this.data = data;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); //we should do this after we set the other values
    }

    public String calculateHash() {
        String hash = StringUtil.applySha256(previousHash
                                            + data
                                            + Long.toString(timeStamp)
                                            + Integer.toString(nonce));
        return hash;
    }

    public void mineBlock(int difficulty) {
        //create a string contains difficulty * '0'.
        String target = new String(new char[difficulty]).replace('\0', '0');
        //loop until the hash of the Block match the requirement of target.
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block mined!!!");
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}
