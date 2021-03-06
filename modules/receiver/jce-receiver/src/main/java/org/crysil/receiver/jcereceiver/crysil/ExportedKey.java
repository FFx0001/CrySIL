package org.crysil.receiver.jcereceiver.crysil;

import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;

import org.crysil.protocol.payload.crypto.key.Key;

/**
 * The Class ExportedKey.
 */
public class ExportedKey extends Key {
    
	private static final long serialVersionUID = 8859206143888053866L;

	/** The private key. */
    protected KeySpec privateKey;
    
    /** The x509 certificate. */
    protected X509Certificate x509Certificate;

    /**
     * Gets the private key.
     *
     * @return the private key
     */
    public KeySpec getPrivateKey() {
        return privateKey;
    }

    /**
     * Sets the private key.
     *
     * @param privateKey the new private key
     */
    public void setPrivateKey(KeySpec privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * Gets the x509 certificate.
     *
     * @return the x509 certificate
     */
    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    /**
     * Sets the x509 certificate.
     *
     * @param x509Certificate the new x509 certificate
     */
    public void setX509Certificate(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    /* (non-Javadoc)
     * @see org.crysil.protocol.payload.crypto.key.Key#getBlankedClone()
     */
    @Override
    public Key getBlankedClone() {
        return this;
    }

    //TODO: ...
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
}
