import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PublicKey publicKey;
    public PrivateKey privateKey;

    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public Wallet() throws InvalidAlgorithmParameterException{
        generate_KeyPair();
    }

    public void generate_KeyPair() throws InvalidAlgorithmParameterException{
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec eSpec = new ECGenParameterSpec("prime192v1");
            keygen.initialize(eSpec, random);
            KeyPair keyPair = keygen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } 
        catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public float getBalance(){
        float total = 0;
        for(Map.Entry<String,TransactionOutput> item: BChain.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) {
                UTXOs.put(UTXO.id, UTXO);
                total = total+UTXO.value;
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey _recepient,Float value) throws InvalidKeyException, SignatureException{
        if (getBalance()<value) {
            System.out.println("Not enough funds to do a Transaction");
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for(Map.Entry<String,TransactionOutput> item:UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total = total +UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total>value) {
                break;
            }
        }
        Transaction newTransaction = new Transaction(publicKey, _recepient, total, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            UTXOs.remove(input.transaction_Outputid);
        }

        return newTransaction;
    }
}
