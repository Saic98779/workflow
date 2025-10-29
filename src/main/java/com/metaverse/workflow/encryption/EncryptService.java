package com.metaverse.workflow.encryption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.dto.CentralRampRequestDto;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncryptService {

    private final String privateKeyPath = "keys/private_key.pem";
    private final String destinationPublicKeyPath = "keys/public_key.pem";

    private static final int GCM_TAG_LENGTH = 128;
    private static final int TAG_BYTE_LENGTH = 16;
    private static final int IV_LENGTH = 12;

    /**
     * Main orchestrator method — handles full encryption & signing flow.
     */
    public String encryptAndSign(String jsonPayload) throws Exception {
        // Step 1: Generate AES-256 key
        byte[] dynamicKey = generateAESKey();

        // Step 3: Derive IV (first 12 bytes of AES key)
        byte[] iv = deriveIV(dynamicKey);

        // Step 4: AES-GCM encrypt payload
        byte[][] aesParts = encryptWithAES(jsonPayload, dynamicKey, iv);
        byte[] ciphertext = aesParts[0];
        byte[] tag = aesParts[1];

        // Step 5: Sign the ciphertext using RSA private key
        byte[] signature = signCiphertext(ciphertext);

        // Step 6: Encrypt AES key with destination’s RSA public key
        byte[] encryptedKey = encryptAESKeyWithRSA(dynamicKey);

        // Step 7: Encode all parts to Base64 and build final payload
        String finalPayload = buildFinalPayload(encryptedKey, ciphertext, signature, tag);

        return "{\"payload\":\"" + finalPayload + "\"}";
    }

    // -------------------------------
    // Step Implementations
    // -------------------------------

    private String convertToJson(CentralRampRequestDto payload) throws Exception {
        return new ObjectMapper().writeValueAsString(payload);
    }

    private byte[] generateAESKey() {
        byte[] dynamicKey = new byte[32];
        new SecureRandom().nextBytes(dynamicKey);
        return dynamicKey;
    }

    private byte[] deriveIV(byte[] dynamicKey) {
        return Arrays.copyOfRange(dynamicKey, 0, IV_LENGTH);
    }

    private byte[][] encryptWithAES(String data, byte[] aesKeyBytes, byte[] iv) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(aesKeyBytes, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec);

        byte[] cipherWithTag = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        byte[] ciphertext = Arrays.copyOfRange(cipherWithTag, 0, cipherWithTag.length - TAG_BYTE_LENGTH);
        byte[] tag = Arrays.copyOfRange(cipherWithTag, cipherWithTag.length - TAG_BYTE_LENGTH, cipherWithTag.length);

        return new byte[][]{ciphertext, tag};
    }

    private byte[] signCiphertext(byte[] ciphertext) throws Exception {
        PrivateKey privateKey = KeyUtil.loadPrivateKey(privateKeyPath);
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(ciphertext);
        return signer.sign();
    }

    private byte[] encryptAESKeyWithRSA(byte[] aesKeyBytes) throws Exception {
        PublicKey publicKey = KeyUtil.loadPublicKey(destinationPublicKeyPath);
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return rsaCipher.doFinal(aesKeyBytes);
    }

    private String buildFinalPayload(byte[] encryptedKey, byte[] ciphertext, byte[] signature, byte[] tag) {
        String sEncKey = Base64.getEncoder().encodeToString(encryptedKey);
        String sCipher = Base64.getEncoder().encodeToString(ciphertext);
        String sSig = Base64.getEncoder().encodeToString(signature);
        String sTag = Base64.getEncoder().encodeToString(tag);

        String joined = String.join(":", sEncKey, sCipher, sSig, sTag);
        return Base64.getEncoder().encodeToString(joined.getBytes(StandardCharsets.UTF_8));
    }
}
