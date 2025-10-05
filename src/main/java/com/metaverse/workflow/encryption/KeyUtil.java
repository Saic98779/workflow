package com.metaverse.workflow.encryption;

import org.springframework.core.io.ClassPathResource;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtil {

    public static PrivateKey loadPrivateKey(String path) throws Exception {
        String key = new String(Files.readAllBytes(new ClassPathResource(path).getFile().toPath()))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static PublicKey loadPublicKey(String path) throws Exception {
        String key = new String(Files.readAllBytes(new ClassPathResource(path).getFile().toPath()))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}

