import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Blockchain {

    public static ArrayList<Block> blockchains = new ArrayList<Block>();
    public static int difficulty = 6;

    public static boolean isChainValid() {
        Block currentBlock = null;
        Block previousBlock = null;

        for (int i = 1; i < blockchains.size(); i++) {
            currentBlock = blockchains.get(i);
            previousBlock = blockchains.get(i - 1);
            String hashTarget = new String(new char[difficulty]).replace('\0', '0');

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        //calculate the time that mining blocks took
        long startTime = System.currentTimeMillis();

        //add our blocks to the blockchain ArrayList:
        blockchains.add(new Block("Hi im the first block", "0"));
        System.out.println("Trying to Mine block 1... ");
        blockchains.get(0).mineBlock(difficulty);

        blockchains.add(new Block("Yo im the second block",blockchains.get(blockchains.size()-1).getHash()));
        System.out.println("Trying to Mine block 2... ");
        blockchains.get(1).mineBlock(difficulty);

        blockchains.add(new Block("Hey im the third block",blockchains.get(blockchains.size()-1).getHash()));
        System.out.println("Trying to Mine block 3... ");
        blockchains.get(2).mineBlock(difficulty);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("\nMining took " + totalTime / 1000 + " seconds");

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchains);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }
}
