package user_management.user_management.auth;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Tiny helper for loading PEM‑formatted RSA keys without any external library.
 */
public final class PemUtils {

    private PemUtils() {}

    /** Strip BEGIN/END header/footer and all whitespace. */
    private static String stripPem(String pem) {
        return pem.replaceAll("-----BEGIN (.*)-----", "")
                  .replaceAll("-----END (.*)-----", "")
                  .replaceAll("\\s", "");
    }

    public static RSAPrivateKey readPrivateKey(String pem) throws Exception {
        byte[] der = Base64.getDecoder().decode(stripPem(pem));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static RSAPublicKey readPublicKey(String pem) throws Exception {
        byte[] der = Base64.getDecoder().decode(stripPem(pem));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
