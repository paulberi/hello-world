package se.metria.xplore.authorization;

import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class JwtVerifier {
    private JWTVerifier verifier;

    public JwtVerifier(String certsUrl) throws MalformedURLException {
        this.verifier = buildJWTVerifier(certsUrl);
    }

    public static final JWTVerifier buildJWTVerifier(String certsUrl) throws MalformedURLException {
        JwkProvider urlJwkProvider = new UrlJwkProvider(new URL(certsUrl));
        JwkProvider cachedJwkprovider = new GuavaCachedJwkProvider(urlJwkProvider);
        RSAKeyProvider keyProvider = new RSAKeyProvider() {
            @Override
            public RSAPublicKey getPublicKeyById(String kid) {
                // Received 'kid' value might be null if it wasn't defined in the Token's header
                if (kid == null) {
                    throw new JWTVerificationException("No kid defined in token");
                }
                try {
                    Jwk jwk = cachedJwkprovider.get(kid);
                    return (RSAPublicKey) jwk.getPublicKey();
                } catch (JwkException e) {
                    throw new JWTVerificationException(e.getMessage());
                }
            }

            @Override
            public RSAPrivateKey getPrivateKey() {
                return null;
            }

            @Override
            public String getPrivateKeyId() {
                return null;
            }
        };

        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        return JWT.require(algorithm).build();
    }

    public DecodedJWT verify(String token) {
        return verifier.verify(token);
    }

    public static boolean verify(String issuer, String token) {
        try
        {
            JWTVerifier verifier = buildJWTVerifier(issuer);
            return verifier.verify(token) != null;
        }
        catch (MalformedURLException e) {
            return false;
        }
    }
}