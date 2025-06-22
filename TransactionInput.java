
//Balance is just the unspent transaction outputs 

public class TransactionInput {
    public String transaction_Outputid;
    public TransactionOutput UTXO; //Acronym for Unspeant transaction output

    public TransactionInput(String transaction_Outputid){
        this.transaction_Outputid = transaction_Outputid;
    }
}
