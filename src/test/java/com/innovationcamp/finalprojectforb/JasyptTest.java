package com.innovationcamp.finalprojectforb;

import com.innovationcamp.finalprojectforb.config.JasyptConfig;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JasyptTest extends JasyptConfig {

    @Test
    public void jasypt_encrypt_decrypt_test() {
        String plainText = "";

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword("");

        String encryptedText = jasypt.encrypt(plainText);
        String decryptedText = jasypt.decrypt(encryptedText);

        System.out.println("encryptedText > " + "ENC(" + encryptedText +")");
        System.out.println("decryptedText > " + decryptedText);

        assertThat(plainText).isEqualTo(decryptedText);
    }
}
