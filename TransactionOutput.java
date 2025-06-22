import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey recipent; 
    public float value; //coins that reciever has
    public String parentTransactionID; //this outputs transaction id

    public TransactionOutput(PublicKey recipent,float value,String parentTransactionID){
        this.recipent = recipent;
        this.value = value;
        this.parentTransactionID = parentTransactionID;
        this.id = StringUtil.applySHA256(StringUtil.getStringFromKey(recipent)+Float.toString(value)+parentTransactionID);
    }

    public boolean isMine(PublicKey publicKey){
        return(publicKey == recipent);
    }
}

