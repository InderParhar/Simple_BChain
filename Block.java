
import java.util.Date;

public class Block {

    public String hash;
    public String previous_hash;

    private String data;
    private long timestamp;

    private int nonce; 

    public Block(String data, String previous_hash) {
        this.data = data;
        this.previous_hash = previous_hash;
        this.timestamp = new Date().getTime();

        this.hash = calculatehash();
    }

    public String calculatehash() {
        String calculated_hash = StringUtil.applySHA256(previous_hash + Long.toString(timestamp) +Integer.toString(nonce)+ data);
        return calculated_hash;
    }

    public void mineBlock(int difficulty){
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculatehash();
            System.out.println("Trying nonce: " + nonce + " | Hash: " + hash);
        }
        System.out.println("Block Mined:"+hash );
    }
}
