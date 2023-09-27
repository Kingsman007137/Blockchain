/** This class will be used to reference TransactionOutputs that have not yet been spent.
 * The transactionOutputId will be used to find the relevant TransactionOutput, allowing
 * miners to check your ownership.
 */
public class TransactionInput {
    //Reference to TransactionOutputs -> transactionId
    private String transactionOutputId;
    //Contains the Unspent transaction output
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }
}
