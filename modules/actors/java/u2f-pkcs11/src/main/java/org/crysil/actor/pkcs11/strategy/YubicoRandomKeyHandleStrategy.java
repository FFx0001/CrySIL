package org.crysil.actor.pkcs11.strategy;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.crysil.actor.pkcs11.Pkcs11KeyStore;

import com.google.common.primitives.Bytes;

/**
 * This emulates the U2F key generation from Yubico: Private key is generated from HMAC over appParam and random bytes.
 * External MAC is HMAC over appParam and private key. The exported key handle is concatenation of external MAC and
 * random bytes. For the authentication requests, the relying party will send the key handle, and we can verify
 * authenticity by performing the same steps as for key generation.
 * 
 * Note: We do not actually use the private key to perform signature operations later on, we only verify authenticity of
 * the key handle provided by the relying party in the U2F protocol.
 */
public class YubicoRandomKeyHandleStrategy extends U2FKeyHandleStrategy {

	private static final int MAC_LEN = 32;
	private static final int RANDOM_LEN = 32;
	private static final int HANDLE_LEN = MAC_LEN + RANDOM_LEN;

	@Override
	protected boolean verifyKeyHandle(byte[] keyHandle, byte[] appParam, byte[] clientParam, Pkcs11KeyStore keyStore) {
		try {
			byte[] calculatedKeyHandle = generateKeyHandleAndRandom(keyHandle, appParam, keyStore).first;
			return Arrays.equals(keyHandle, calculatedKeyHandle);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Tuple<byte[], byte[]> generateKeyHandleAndRandom(byte[] encodedRandom, byte[] appParam,
			Pkcs11KeyStore keyStore) throws Exception {
		byte[] random = new byte[RANDOM_LEN];
		byte[] keyHandle = new byte[HANDLE_LEN];
		byte[] existingMac = null;

		if (encodedRandom != null && encodedRandom.length > 0) {
			existingMac = new byte[MAC_LEN];
			keyHandle = encodedRandom;
			System.arraycopy(keyHandle, 0, existingMac, 0, MAC_LEN);
			System.arraycopy(keyHandle, MAC_LEN, random, 0, RANDOM_LEN);
		} else {
			SecureRandom.getInstance("SHA1PRNG").nextBytes(random);
		}

		// We need a secret (symmetric key) out of the available private (asymmetric) key on the card
		Signature signature = Signature.getInstance(SIG_RSA, keyStore.getProviderName());
		signature.initSign(keyStore.getKey(RSA_KEY_ID).getPrivateKey());
		signature.update("super secret u2f".getBytes("UTF-8"));
		byte[] key = signature.sign();
		Key secretKey = new SecretKeySpec(key, "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");

		// private key = HMAC[key](appParam || random)
		mac.init(secretKey);
		mac.update(appParam);
		mac.update(random);
		byte[] privateKeyBytes = mac.doFinal();

		// externalMac = HMAC[key](appParam || privateKey)
		mac.init(secretKey);
		mac.update(appParam);
		mac.update(privateKeyBytes);
		byte[] externalMac = mac.doFinal();

		// Check whether externally provided MAC matches the calculated MAC
		if (existingMac != null && !Arrays.equals(externalMac, existingMac)) {
			throw new Exception("MACs do not match, invalid keyHandle given");
		}

		// keyHandle = (externalMac || random)
		keyHandle = Bytes.concat(externalMac, random);
		return new Tuple<byte[], byte[]>(keyHandle, keyHandle);
	}
}
