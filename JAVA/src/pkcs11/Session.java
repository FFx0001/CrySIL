package pkcs11;
import java.util.ArrayList;

import objects.Mechanism;
import objects.MechanismObject;

import proxys.CK_MECHANISM;
import proxys.CK_NOTIFY_CALLBACK;
import proxys.CK_BYTE_ARRAY;
import proxys.CK_ULONG_JPTR;
//import proxys.CK_VOID_PTR;
import proxys.RETURN_TYPE;
import proxys.SESSION_STATE;


public class Session {
	public enum ACCESS_TYPE {
		RO,RW
	}
	public enum USER_TYPE {
		PUBLIC,USER,SO
	}
//	private USER_TYPE utype;
	private ACCESS_TYPE atype;
	private byte[] pApplication;
	private CK_NOTIFY_CALLBACK notify_callback;
	private Slot slot;
	private long flags;
	private long handle;
	
	private CryptoHelper signHelper;
	private CryptoHelper verifyHelper;//TODO
	private CryptoHelper decryptHelper;
	private CryptoHelper encryptHelper;
	private FindObjectsHelper findObjectsHelper;

	public Session(Slot slot,long handle,ACCESS_TYPE atype){
	//	this.flags = flags;
		this.atype = atype;
		this.slot = slot;
		this.handle = handle;
	}
	public boolean isRW(){
		return (atype==ACCESS_TYPE.RW)?true:false;
	}
	//handle = SlotID + SessionID
	public long getHandle(){
		return handle;
	}
	//ID = SessionID (Slot local)
	protected long getID(){
		return handle%Slot.MAX_SESSIONS_PER_SLOT;
	}
	public Slot getSlot(){
		return slot;
	}
	public Slot getToken(){
		return slot;
	}
	public void signInit(CK_MECHANISM pMechanism, long hKey) throws PKCS11Error{
		if(signHelper != null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_ACTIVE);
		}
		signHelper = new CryptoHelper(getToken().checkAndInit(hKey,pMechanism,"sign"));
	}
	public void sign(byte[] pData){
		if(signHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		try{
			signHelper.addData(pData);
		}catch(PKCS11Error e){
			signHelper = null; //Operation is canceld if any error happens
			throw e;
		}
	}
	public byte[] signGetData() throws PKCS11Error{
		if(signHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		try{
			if(!signHelper.hasProcessedData()){
				byte[] signed_data = getToken().sign(signHelper.getData(), signHelper.getParams());
				signHelper.setProcessedData(signed_data);
			}
			return signHelper.getProcessedData();
		}catch(PKCS11Error e){
			signHelper = null;
			throw e;
		}
	}
	public void signFinal() throws PKCS11Error{
		if(signHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		signHelper = null;
	}
	
	public void verifyInit(CK_MECHANISM pMechanism, long hKey) throws PKCS11Error{
		if(verifyHelper!= null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_ACTIVE);
		}
		verifyHelper = new CryptoHelper(getToken().checkAndInit(hKey,pMechanism,"verify"));
	}
	public void verifySetData(byte[] pData){
		if(verifyHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		try{
			verifyHelper.addData(pData);
		}catch(PKCS11Error e){
			verifyHelper = null; //Operation is canceld if any error happens
			throw e;
		}
	}
	public boolean verify(byte[] signature){
		if(verifyHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		try{
			return getToken().verify(signature,verifyHelper.getData(),verifyHelper.getParams());
		}catch(PKCS11Error e){
			verifyHelper = null; //Operation is canceld if any error happens
			throw e;
		}
	}
	public void verifyFinal() throws PKCS11Error{
		if(verifyHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		verifyHelper = null;
	}
	
	
	public void decryptInit(CK_MECHANISM pMechanism, long hKey){
		if(decryptHelper != null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_ACTIVE);
		}
		decryptHelper = new CryptoHelper(getToken().checkAndInit(hKey,pMechanism,"decrypt"));
	}
	public void decrypt(byte[] encdata){
		if(decryptHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		try{
			byte[] plain_data = getToken().decrypt(encdata,decryptHelper.getParams());
			decryptHelper.setProcessedData(plain_data);
		}catch(PKCS11Error e){
			decryptHelper = null;
			throw e;
		}
	}
	public byte[] decryptGetData(){
		if(decryptHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		return decryptHelper.getProcessedData();
	}
	public void decryptFinal(){
		if(decryptHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		decryptHelper = null;
	}

	public void encryptInit(CK_MECHANISM pMechanism, long hKey){
		if(encryptHelper != null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_ACTIVE);
		}
		decryptHelper = new CryptoHelper(getToken().checkAndInit(hKey,pMechanism,"decrypt"));
	}
	public void encrypt(byte[] data){
		if(encryptHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		try{
			byte[] encdata = getToken().encrypt(data,encryptHelper.getParams());
			encryptHelper.setProcessedData(encdata);
		}catch(PKCS11Error e){
			encryptHelper = null;
			throw e;
		}
	}
	public byte[] encryptGetData(){
		if(encryptHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		return encryptHelper.getProcessedData();
	}
	public void encryptFinal(){
		if(encryptHelper == null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_NOT_INITIALIZED);
		}
		encryptHelper = null;
	}
	
	
	public void initFind(FindObjectsHelper f) throws PKCS11Error{
		if(findObjectsHelper != null){
			throw new PKCS11Error(RETURN_TYPE.OPERATION_ACTIVE);
		}
		findObjectsHelper = f;
	}
}
