
import java.util.Date;

public class Block {

    public String hash;
    public String previous_hash;

    public String data;
    public long timestamp;

    public Block(String data, String previous_hash) {
        this.data = data;
        this.previous_hash = previous_hash;
        this.timestamp = new Date().getTime();

        this.hash = calculatehash();
    }

    public String calculatehash() {
        String calculated_hash = StringUtil.applySHA256(previous_hash + Long.toString(timestamp) + data);
        return calculated_hash;
    }
}
