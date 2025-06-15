import java.util.ArrayList;

public class BChain {

    public static ArrayList<Block> blockchain =  new ArrayList<Block>();
    public static void main(String[] args) {
        
        Block firstBlock = new Block("First Block", "0");
        System.out.println("Hash for block 1: " +firstBlock.hash);

        Block SecondBlock = new Block("Second Block", firstBlock.hash);
        System.out.println("Hash for block 2: " +SecondBlock.hash);

        Block ThridBlock = new Block("Third Block", SecondBlock.hash);
        System.out.println("Hash for block 3: " +ThridBlock.hash);

        // String bchain_json = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
    }
}
