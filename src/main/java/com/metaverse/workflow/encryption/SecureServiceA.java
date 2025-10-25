package com.metaverse.workflow.encryption;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.metaverse.workflow.dto.CentralRampRequestDto;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SecureServiceA {

    private static final int AES_KEY_SIZE = 32; // 256-bit
    private static final int IV_SIZE = 16;      // first 16 bytes of AES key as IV
    private static final int GCM_TAG_LENGTH = 128;

    private final PrivateKey appAPrivateKey;
    private final PublicKey appBPublicKey;
    private final PrivateKey appBPrivateKey;
    private final PublicKey appAPublicKey;

    public SecureServiceA() throws Exception {
        this.appAPrivateKey = KeyUtilA.loadPrivateKey("keys/AppA_private.pem");
        this.appBPublicKey = KeyUtilA.loadPublicKey("keys/AppB_public.pem");
        this.appBPrivateKey = KeyUtil.loadPrivateKey("keys/AppB_private.pem");
        this.appAPublicKey = KeyUtil.loadPublicKey("keys/AppA_public.pem");
    }


    public String encryptAndSign(CentralRampRequestDto payload) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(payload);

        // 1. Generate 32-byte AES key
        byte[] aesKey = new byte[32];
        new SecureRandom().nextBytes(aesKey);

        // 2. IV = first 12 bytes
        byte[] iv = Arrays.copyOf(aesKey, 12);

        // 3. AES/GCM encryption
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        aesCipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

        byte[] encryptedWithTag = aesCipher.doFinal(payloadJson.getBytes(StandardCharsets.UTF_8));

        // 4. Split ciphertext and tag (last 16 bytes)
        int tagLength = 16;
        byte[] ciphertext = Arrays.copyOf(encryptedWithTag, encryptedWithTag.length - tagLength);
        byte[] tag = Arrays.copyOfRange(encryptedWithTag, encryptedWithTag.length - tagLength, encryptedWithTag.length);

        // 5. Sign only ciphertext
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(appBPrivateKey); // PHP consuming server private key
        signer.update(ciphertext);
        byte[] signature = signer.sign();

        // 6. Encrypt AES key with RSA public key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, appAPublicKey); // PHP destination server public key
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey);

        // 7. Base64 encode all parts separately
        String b64Key = Base64.getEncoder().encodeToString(encryptedAesKey);
        String b64Cipher = Base64.getEncoder().encodeToString(ciphertext);
        String b64Sig = Base64.getEncoder().encodeToString(signature);
        String b64Tag = Base64.getEncoder().encodeToString(tag);

        // 8. Combine as "key:ciphertext:signature:tag"
        String combined = b64Key + ":" + b64Cipher + ":" + b64Sig + ":" + b64Tag;

        // 9. Base64 wrap the entire string
        return Base64.getEncoder().encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }



    public CentralRampRequestDto decryptAndVerify(String base64Combined) throws Exception {
        byte[] combinedBytes = Base64.getDecoder().decode(base64Combined);
        String combined = new String(combinedBytes, StandardCharsets.UTF_8);

        String[] parts = combined.split(":");
        if(parts.length != 3) throw new IllegalArgumentException("Invalid payload");

        String encryptedKeyBase64 = parts[0];
        String encryptedPayloadBase64 = parts[1];
        String signatureBase64 = parts[2];

        // 1️⃣ Decrypt AES key
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        rsaCipher.init(Cipher.DECRYPT_MODE, appBPrivateKey);
        byte[] aesKey = rsaCipher.doFinal(Base64.getDecoder().decode(encryptedKeyBase64));
        SecretKeySpec secretKey = new SecretKeySpec(aesKey, "AES");

        // 2️⃣ Derive IV from first 16 bytes of AES key
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(aesKey, 0, iv, 0, IV_SIZE);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        // 3️⃣ Verify signature on Base64 payload
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(appAPublicKey);
        verifier.update(encryptedPayloadBase64.getBytes(StandardCharsets.UTF_8));
        boolean valid = verifier.verify(Base64.getDecoder().decode(signatureBase64));
        if(!valid) throw new SecurityException("Invalid signature");

        // 4️⃣ Decrypt AES-GCM payload
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
        byte[] decryptedBytes = aesCipher.doFinal(Base64.getDecoder().decode(encryptedPayloadBase64));

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(decryptedBytes, CentralRampRequestDto.class);
    }
}
