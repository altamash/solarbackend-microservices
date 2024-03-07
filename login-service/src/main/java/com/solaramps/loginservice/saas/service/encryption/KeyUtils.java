package com.solaramps.loginservice.saas.service.encryption;

import com.solaramps.loginservice.saas.model.attribute.SystemAttribute;
import com.solaramps.loginservice.saas.service.systemAttribute.ESystemAttribute;
import com.solaramps.loginservice.saas.service.systemAttribute.SystemAttributeService;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class KeyUtils {

    @Autowired
    private SystemAttributeService systemAttributeService;

    // Generate Keys /////////////////////////////////////////////
    public static SecretKey generateSymmetricKey(String algorithm, int keySize) {
        KeyGenerator generator = null;
        try {
//            generator = KeyGenerator.getInstance("AES");
            generator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception caught while generating secret key: " + e);
        }
//        generator.init(128);
        generator.init(keySize);
        return generator.generateKey();
    }

    public static KeyPair generateAsymmetricKeys(String algorithm, int keySize) {
        KeyPair keyPair = null;
        try {
//            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            // Initializing KeyPairGenerator
//            keyGen.initialize(2048, random);
            keyGen.initialize(keySize, random);

            // Generate keys
            keyPair = keyGen.generateKeyPair();

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    // Load Keys /////////////////////////////////////////////
    public SecretKey loadSecretKey(String algorithm) {
        SystemAttribute secretKeyAttribute =
                systemAttributeService.findByAttributeKey(ESystemAttribute.SECRET_KEY.getAttributeKey());
        if (secretKeyAttribute != null) {
            byte[] bytes = secretKeyAttribute.getAttributeValueLob();
            return new SecretKeySpec(bytes, 0, bytes.length, algorithm);
        }
        return null;
    }

    public PublicKey loadPublicKey(String algorithm, byte[] bytes) {
        PublicKey publicKey = null;
        if (bytes != null) {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return publicKey;
    }

    public PublicKey loadPublicKey(String algorithm) {
        SystemAttribute publicKeyAttribute =
                systemAttributeService.findByAttributeKey(ESystemAttribute.PUBLIC_KEY.getAttributeKey());
        if (publicKeyAttribute != null) {
            return loadPublicKey(algorithm, publicKeyAttribute.getAttributeValueLob());
        }
        return null;
    }

    public PublicKey loadPublicKeyFromFile(String algorithm, File file) {
        PublicKey publicKey = null;
        byte[] bytes = new byte[0];
        try (FileReader reader = new FileReader(file);
             PemReader pemReader = new PemReader(reader)) {
            PemObject pemObject = pemReader.readPemObject();
            bytes = pemObject.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadPublicKey(algorithm, bytes);
    }

    public PublicKey loadPublicKeyFromString(String algorithm, String pemString) {
        PublicKey publicKey = null;
        byte[] bytes = new byte[0];
        try (PemReader pemReader = new PemReader(new StringReader(pemString))) {
            PemObject pemObject = pemReader.readPemObject();
            bytes = pemObject.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadPublicKey(algorithm, bytes);
    }

    public PrivateKey loadPrivateKey(String algorithm) {
        PrivateKey privateKey = null;
        SystemAttribute privateKeyAttribute =
                systemAttributeService.findByAttributeKey(ESystemAttribute.PRIVATE_KEY.getAttributeKey());
        if (privateKeyAttribute != null) {
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyAttribute.getAttributeValueLob());
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                privateKey = keyFactory.generatePrivate(privateKeySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return privateKey;
    }

}
