
import java.security.*;
import java.util.ArrayList;

public class Transaction {

    public String transactionid;
    public PublicKey senderKey;
    public PublicKey receiverKey;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.senderKey = from;
        this.receiverKey = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++;
        return StringUtil.applySHA256(
                StringUtil.getStringFromKey(senderKey) +
                        StringUtil.getStringFromKey(receiverKey) +
                        Float.toString(value) + sequence);
    }

    public void generateSignature(PrivateKey privateKey) throws InvalidKeyException, SignatureException {
        String data = StringUtil.getStringFromKey(senderKey) + StringUtil.getStringFromKey(receiverKey)
                + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() throws InvalidKeyException, SignatureException {
        String data = StringUtil.getStringFromKey(senderKey) + StringUtil.getStringFromKey(receiverKey)
                + Float.toString(value);
        return StringUtil.verifyECDSASig(senderKey, data, signature);
    }

    //return sum of inputs(UTXO) values
    public float getInputs_value() 
    {
        float total = 0;
        for (TransactionInput i : inputs) {
            if (i.UTXO == null) {
                total = total + i.UTXO.value; //If transaction can't be found skip it
            }
        }
        return total;
    }

    //return sum of outputs
    public float getOutputs_value(){
        float total = 0;
        for(TransactionOutput o: outputs){
            total = total + o.value;
        }
        return total;
    }

    public boolean process_Transaction() throws InvalidKeyException, SignatureException{

        if (verifySignature() ==false) {
            System.out.println("Failed to Verify Signature");
            return false;
        }

        for(TransactionInput i : inputs){
            i.UTXO = BChain.UTXOs.get(i.transaction_Outputid);
        }

        if (getInputs_value()<BChain.minimumTransaction) {
            System.out.println("Trasaction Input to small: "+ getInputs_value());
            return false;
        }

        float leftOver = getInputs_value() - value;
        transactionid = calculateHash();
        outputs.add(new TransactionOutput(this.receiverKey, value, transactionid)); //send value
        outputs.add(new TransactionOutput(this.senderKey, leftOver, transactionid)); // send left over to sender

        //add outputs to unspent list
        for(TransactionInput i : inputs){
            BChain.UTXOs.remove(i.UTXO.id);
        }
        return true;
    }
    
}
