
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

    private String calculateHash(){
        sequence++;
        return StringUtil.applySHA256(
            StringUtil.getStringFromKey(senderKey) +
            StringUtil.getStringFromKey(receiverKey) +
            Float.toString(value) +sequence
        );
    }

    public void generateSignature(PrivateKey privateKey) throws InvalidKeyException, SignatureException{
        String data = StringUtil.getStringFromKey(senderKey) + StringUtil.getStringFromKey(receiverKey)+Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean  verifySignature() throws InvalidKeyException, SignatureException{
        String data = StringUtil.getStringFromKey(senderKey) + StringUtil.getStringFromKey(receiverKey)+Float.toString(value);
        return StringUtil.verifyECDSASig(senderKey, data, signature);
    }
}
