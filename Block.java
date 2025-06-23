
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;

public class Block {

    public String hash;
    public String previous_hash;

    private String data;
    private long timestamp;

    private int nonce; 

    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Block(String previous_hash) {
        // this.data = data;
        this.previous_hash = previous_hash;
        this.timestamp = new Date().getTime();

        this.hash = calculatehash();
    }

    public String calculatehash() {
        String calculated_hash = StringUtil.applySHA256(previous_hash + Long.toString(timestamp) +Integer.toString(nonce)+ merkleRoot);
        return calculated_hash;
    }

    public void mineBlock(int difficulty){
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty);
        // String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculatehash();
            // System.out.println("Trying nonce: " + nonce + " | Hash: " + hash);
        }
        System.out.println("Block Mined:"+hash );
    }
    public boolean addTransaction(Transaction transaction) throws InvalidKeyException, SignatureException{
        if(transaction== null){
            return false;
        }
        if (previous_hash != "0") {
            if ((transaction.process_Transaction()!=true)) {
                System.out.println("Transaction failed to process");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction successfully added to block");
        return true;
    }
}
