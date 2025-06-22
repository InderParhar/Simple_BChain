import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    public PublicKey publicKey;
    public PrivateKey privateKey;

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
}
