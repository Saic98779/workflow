package com.metaverse.workflow.encryption;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SecureService {

    private static final int IV_SIZE = 16;
    private static final int GCM_TAG_LENGTH = 128;

    private static final int AES_KEY_SIZE = 32; // 256-bit

    private final PrivateKey appBPrivateKey;
    private final PublicKey appAPublicKey;

    public SecureService() throws Exception {
        this.appBPrivateKey = KeyUtil.loadPrivateKey("keys/private_key.pem");
        this.appAPublicKey = KeyUtil.loadPublicKey("keys/public_key.pem");
    }

    public Employee decryptAndVerify(String base64Combined) throws Exception {
        byte[] combinedBytes = Base64.getDecoder().decode(base64Combined);
        String combined = new String(combinedBytes, StandardCharsets.UTF_8);

        String[] parts = combined.split(":");
        if(parts.length != 3) throw new IllegalArgumentException("Invalid payload");

        String encryptedKeyBase64 = parts[0];
        String encryptedPayloadBase64 = parts[1];
        String signatureBase64 = parts[2];

        // AES key decrypt
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        rsaCipher.init(Cipher.DECRYPT_MODE, appBPrivateKey);
        byte[] aesKey = rsaCipher.doFinal(Base64.getDecoder().decode(encryptedKeyBase64));

        // Verify signature
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(appAPublicKey);
        verifier.update(encryptedPayloadBase64.getBytes(StandardCharsets.UTF_8));
        boolean valid = verifier.verify(Base64.getDecoder().decode(signatureBase64));
        if(!valid) throw new SecurityException("Invalid signature");

        // AES-GCM decrypt
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(aesKey, 0, iv, 0, IV_SIZE);
        SecretKeySpec secretKey = new SecretKeySpec(aesKey, "AES");

        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
        byte[] decryptedBytes = aesCipher.doFinal(Base64.getDecoder().decode(encryptedPayloadBase64));

        ObjectMapper mapper = new ObjectMapper();
        Employee employee = mapper.readValue(decryptedBytes, Employee.class);

        return employee;
    }

    public String encryptAndSign(Employee payload) throws Exception {
        // 1. Generate AES key and derive IV from first 12 bytes
        byte[] aesKey = new byte[AES_KEY_SIZE];
        new SecureRandom().nextBytes(aesKey);
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(aesKey, 0, iv, 0, IV_SIZE); // Same as PHP substr($key, 0, 12)

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(payload);

        // 2. AES-GCM Encryption
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec secretKey = new SecretKeySpec(aesKey, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

        byte[] encryptedPayloadBytes = aesCipher.doFinal(payloadJson.getBytes(StandardCharsets.UTF_8));
        String encryptedPayloadBase64 = Base64.getEncoder().encodeToString(encryptedPayloadBytes);

        // 3. Sign encrypted payload
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(appBPrivateKey);
        signer.update(encryptedPayloadBase64.getBytes(StandardCharsets.UTF_8));
        byte[] signature = signer.sign();
        String signatureBase64 = Base64.getEncoder().encodeToString(signature);

        // 4. Encrypt AES key with App A public key using OAEP
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, appAPublicKey);
        byte[] encryptedAesKeyBytes = rsaCipher.doFinal(aesKey);
        String encryptedAesKeyBase64 = Base64.getEncoder().encodeToString(encryptedAesKeyBytes);

        // 5. Combine components
        String combined = encryptedAesKeyBase64 + ":" + encryptedPayloadBase64 + ":" + signatureBase64;
        return Base64.getEncoder().encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }
}

