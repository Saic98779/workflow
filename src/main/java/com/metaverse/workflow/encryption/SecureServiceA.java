package com.metaverse.workflow.encryption;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
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
        // 1. Generate AES 256-bit key
        byte[] aesKey = new byte[32];
        new SecureRandom().nextBytes(aesKey);
        SecretKey secretKey = new SecretKeySpec(aesKey, "AES");

        // 2. IV = first 12 bytes
        byte[] iv = Arrays.copyOfRange(aesKey, 0, 12);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);

        // 3. Serialize payload
        ObjectMapper mapper = new ObjectMapper();
        byte[] plainBytes = mapper.writeValueAsBytes(payload);

        // 4. AES-GCM encryption
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
        byte[] cipherWithTag = aesCipher.doFinal(plainBytes);

        // 5. Split ciphertext and tag
        int tagLength = 16;
        byte[] ciphertext = Arrays.copyOf(cipherWithTag, cipherWithTag.length - tagLength);
        byte[] tag = Arrays.copyOfRange(cipherWithTag, cipherWithTag.length - tagLength, cipherWithTag.length);

        // 6. Sign raw ciphertext
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(appAPrivateKey);
        signer.update(ciphertext); // raw ciphertext bytes
        byte[] signatureBytes = signer.sign();

        // 7. Encrypt AES key with destination RSA public key (OAEP SHA-1)
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, appBPublicKey);
        byte[] encryptedKeyBytes = rsaCipher.doFinal(aesKey);

        // 8. Base64 encode individual parts
        String aesKeyBase64 = Base64.getEncoder().encodeToString(encryptedKeyBytes);
        String ciphertextBase64 = Base64.getEncoder().encodeToString(ciphertext);
        String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);
        String tagBase64 = Base64.getEncoder().encodeToString(tag);

        // 9. Combine with ':' separator
        String combined = aesKeyBase64 + ":" + ciphertextBase64 + ":" + signatureBase64 + ":" + tagBase64;

        // 10. Base64 encode the final combined string
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
