import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.GsonBuilder;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;
import java.security.SignatureException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class BChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 3;
    public static Wallet wallet_a;
    public static Wallet wallet_b;

    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static float minimumTransaction = 0.1f;

    public static Transaction genesisTransaction;

    public static Boolean isChainValid() throws InvalidKeyException, SignatureException {
        Block current_Block;
        Block previous_Block;

        String hashtarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        for (int i = 1; i < blockchain.size(); i++) {

            current_Block = blockchain.get(i);
            previous_Block = blockchain.get(i - 1);

            if (!current_Block.hash.equals(current_Block.calculatehash())) {
                System.out.println("Current Hash is not equal");
                return false;
            }

            if (!previous_Block.hash.equals(current_Block.previous_hash)) {
                System.out.println("Previous Hash is not equal");
                return false;
            }

            if (!current_Block.hash.substring(0, difficulty).equals(hashtarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

            TransactionOutput tempOutput;
            for (int t = 0; t < current_Block.transactions.size(); t++) {
                Transaction currentTransaction = current_Block.transactions.get(t);

                if (!currentTransaction.verifySignature()) {
                    System.out.println("Signature on Transaction (" + t + ") is invalid");
                    return false;
                }

                if (currentTransaction.getInputs_value() != currentTransaction.getOutputs_value()) {
                    System.out.println("Inputs are not equal to outputs on transaction(" + t + ")");
                    return false;
                }

                for (TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transaction_Outputid);

                    if (tempOutput == null) {
                        System.out.println("Referenced input on transaction(" + t + ") is missing");
                        return false;
                    }

                    if (input.UTXO.value != tempOutput.value) {
                        System.out.println("Referenced Input in transaction(" + t + ") is invalid");
                        return false;
                    }
                    tempUTXOs.remove(input.transaction_Outputid);
                }

                for (TransactionOutput output : currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if (currentTransaction.outputs.get(0).recipent != currentTransaction.receiverKey) {
                    System.out.println("Transaction (" + t + ") output receiver is not who he should be");
                    return false;
                }

                if (currentTransaction.outputs.get(0).recipent != currentTransaction.senderKey) {
                    System.out.println("Transaction (" + t + ") outpt change is not the sender");
                    return false;
                }
            }
        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static void main(String[] args)
            throws InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {

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

        // String bchain_json = new
        // GsonBuilder().setPrettyPrinting().create().toJson(blockchain);

        // String blockchain_json = new
        // GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        // System.out.println("Block Chain: ");
        // System.out.println(bchain_json);

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        wallet_a = new Wallet();
        wallet_b = new Wallet();
        Wallet coinbase = new Wallet();

        // genesis transactions
        genesisTransaction = new Transaction(coinbase.publicKey, wallet_a.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.transactionid = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.receiverKey, genesisTransaction.value,
                genesisTransaction.transactionid));
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("Creating and Mining genesis block");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        // Tests
        Block block1 = new Block(genesis.hash);
        System.out.println("Wallet A has balance of " + wallet_a.getBalance());
        System.out.println("Wallet A is attempting to send 40 to wallet b ");
        block1.addTransaction(wallet_a.sendFunds(wallet_b.publicKey, 40f));
        addBlock(block1);

        Block block2 = new Block(block1.hash);
        System.out.println("Wallet A is attempting to send mre funds (1000) than he has ");
        block2.addTransaction(wallet_a.sendFunds(wallet_b.publicKey, 1000f));
        addBlock(block2);

        Block block3 = new Block(block2.hash);
        System.out.println("Wallet b is trying to send funds(20) to wallet 1");
        block3.addTransaction(wallet_b.sendFunds(wallet_a.publicKey, 20f));
        System.out.println("Wallet a has" + wallet_a.getBalance());
        System.out.println("Wallet b has" + wallet_b.getBalance());

        isChainValid();

        // System.out.println("Private and public keys are:");
        // System.out.println("Public Key: " +
        // StringUtil.getStringFromKey(wallet_a.privateKey));
        // System.out.println("--------------------------------------");
        // System.out.println("Private Key: " +
        // StringUtil.getStringFromKey(wallet_a.publicKey));
        // System.out.println("--------------------------------------");

        // Transaction transaction = new Transaction(wallet_a.publicKey,
        // wallet_b.publicKey, 5, null);
        // transaction.generateSignature(wallet_a.privateKey);

        // System.out.println("Is Signature valid?");
        // System.out.println("--------------------------------------");
        // System.out.println(transaction.verifySignature());
    }
}
