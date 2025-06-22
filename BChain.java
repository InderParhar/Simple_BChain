import java.util.ArrayList;

import com.google.gson.GsonBuilder;

import java.security.InvalidAlgorithmParameterException;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class BChain {

    public static ArrayList<Block> blockchain =  new ArrayList<Block>();
    public static int difficulty = 5;

    public static Wallet wallet_a;
    public static Wallet wallet_b; 

    public static Boolean isChainValid(){   
        Block current_Block;
        Block previous_Block;

        for(int i =1 ;i<blockchain.size();i++){
            current_Block = blockchain.get(i);
            previous_Block = blockchain.get(i-1);

            if (!current_Block.hash.equals(current_Block.calculatehash())) {
                System.out.println("Current Hash is not equal");
                return false;
            }

            if (!previous_Block.hash.equals(previous_Block.calculatehash())) {
                System.out.println("Previous Hash is not equal");
                return false;                
            }
        }
        return true;   
    }
    public static void main(String[] args) throws InvalidAlgorithmParameterException {
        
        // Block firstBlock = new Block("First Block", "0");
        // blockchain.add(firstBlock);
        // System.out.println("Trying to mine 1");
        // blockchain.get(0).mineBlock(difficulty);

        // Block SecondBlock = new Block("Second Block", firstBlock.hash);
        // blockchain.add(SecondBlock);
        // System.out.println("Trying to mine 2");
        // blockchain.get(1).mineBlock(difficulty);

        // Block ThridBlock = new Block("Third Block", SecondBlock.hash);
        // blockchain.add(ThridBlock);
        // System.out.println("Trying to mine 3");
        // blockchain.get(2).mineBlock(difficulty);

        // System.out.println("\n Blockchain is valid: " + isChainValid());

        // String bchain_json = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);

        // String blockchain_json = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        // System.out.println("Block Chain: ");
        // System.out.println(bchain_json);

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        wallet_a = new Wallet();
        wallet_b = new Wallet();

        System.out.println("Private and public keys are:");
        System.out.println(StringUtil.getStringFromKey(wallet_a.privateKey));
        System.out.println(StringUtil.getStringFromKey(wallet_a.publicKey));

        
    }
}
