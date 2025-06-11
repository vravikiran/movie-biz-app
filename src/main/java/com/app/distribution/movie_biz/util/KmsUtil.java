package com.app.distribution.movie_biz.util;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.distribution.movie_biz.config.KmsConfig;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;

@Component
public class KmsUtil {
	private KmsClient kmsClient;
	@Autowired
	KmsConfig kmsConfig;
	Logger log = LoggerFactory.getLogger(KmsUtil.class);

	public KmsUtil() {
		this.kmsClient = KmsClient.builder().region(Region.AP_SOUTH_1).build();
		this.kmsConfig = new KmsConfig();
	}

	public String kmsEncrypt(String plainText) {
		log.debug("EncryptionUtil::kmsEncrypt: START: ");
		EncryptRequest encryptRequest = buildEncryptRequest(plainText);
		EncryptResponse encryptResponse = kmsClient.encrypt(encryptRequest);
		SdkBytes cipherTextBytes = encryptResponse.ciphertextBlob();
		byte[] base64EncodedValue = Base64.getEncoder().encode(cipherTextBytes.asByteArray());
		String responseBase64 = new String(base64EncodedValue);
		log.debug("EncryptionUtil::kmsEncrypt: RESPONSE: {}", responseBase64);
		log.debug("EncryptionUtil::kmsEncrypt: END: {}", responseBase64);
		return responseBase64;
	}

	public String kmsDecrypt(String base64EncodedValue) {
		log.debug("EncryptionUtil::kmdDecrypt: START: ");
		DecryptRequest decryptRequest = buildDecryptRequest(base64EncodedValue);
		DecryptResponse decryptResponse = this.kmsClient.decrypt(decryptRequest);
		String decryptTest = decryptResponse.plaintext().asUtf8String();
		log.debug("EncryptionUtil::kmdDecrypt: END: ");
		return decryptTest;
	}

	private EncryptRequest buildEncryptRequest(String plainText) {
		log.debug("EncryptionUtil::buildEncryptRequest: START: ");
		SdkBytes plainTextBytes = SdkBytes.fromUtf8String(plainText);
		EncryptRequest encryptRequest = EncryptRequest.builder().keyId(kmsConfig.getKey()).plaintext(plainTextBytes)
				.build();
		log.debug("EncryptionUtil::buildEncryptRequest: END: ");
		return encryptRequest;
	}

	private DecryptRequest buildDecryptRequest(String base64EncodedValue) {
		log.debug("EncryptionUtil::buildDecryptRequest: START: ");
		SdkBytes encryptBytes = SdkBytes.fromByteArray(Base64.getDecoder().decode(base64EncodedValue));
		DecryptRequest decryptRequest = DecryptRequest.builder().keyId(kmsConfig.getKey())
				.ciphertextBlob(encryptBytes).build();
		log.debug("EncryptionUtil::buildDecryptRequest: END: ");
		return decryptRequest;
	}
}