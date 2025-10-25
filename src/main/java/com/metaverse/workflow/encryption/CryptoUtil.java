package com.metaverse.workflow.encryption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.dto.CentralRampRequestDto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class CryptoUtil {
    private PrivateKey loadPrivateKey(String path) throws Exception {
        String key = new String(Files.readAllBytes(new File(path).toPath()))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String path) throws Exception {
        String key = new String(Files.readAllBytes(new File(path).toPath()))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public String encryptAndSign(CentralRampRequestDto payload, String privateKeyPath, String publicKeyPath) throws Exception {
        PrivateKey appBPrivateKey = loadPrivateKey(privateKeyPath);
        PublicKey appAPublicKey = loadPublicKey(publicKeyPath);

        // Generate AES key
        byte[] aesKey = new byte[32];
        new SecureRandom().nextBytes(aesKey);

        byte[] iv = Arrays.copyOf(aesKey, 12);

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(payload);

        // AES/GCM
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        aesCipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
        byte[] encryptedPayloadWithTag = aesCipher.doFinal(payloadJson.getBytes(StandardCharsets.UTF_8));

        // Split ciphertext and tag
        int tagLength = 16;
        byte[] ciphertext = Arrays.copyOf(encryptedPayloadWithTag, encryptedPayloadWithTag.length - tagLength);
        byte[] tag = Arrays.copyOfRange(encryptedPayloadWithTag, ciphertext.length, encryptedPayloadWithTag.length);

        // Sign only ciphertext
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(appBPrivateKey);
        signer.update(ciphertext);
        byte[] signature = signer.sign();

        // Encrypt AES key with public key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, appAPublicKey);
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey);

        // Base64 encode
        String encryptedAesKeyB64 = Base64.getEncoder().encodeToString(encryptedAesKey);
        String ciphertextB64 = Base64.getEncoder().encodeToString(ciphertext);
        String signatureB64 = Base64.getEncoder().encodeToString(signature);
        String tagB64 = Base64.getEncoder().encodeToString(tag);

        // Combine in PHP order
        String combined = encryptedAesKeyB64 + ":" + ciphertextB64 + ":" + signatureB64 + ":" + tagB64;
        return Base64.getEncoder().encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }
}
