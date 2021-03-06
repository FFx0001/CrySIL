import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.crysil.commons.Module;
import org.crysil.communications.http.GateKeeperConfiguration;
import org.crysil.errorhandling.AuthenticationFailedException;
import org.crysil.errorhandling.CrySILException;
import org.crysil.gatekeeperwithsessions.Gatekeeper;
import org.crysil.protocol.Request;
import org.crysil.protocol.Response;
import org.crysil.protocol.header.SessionHeader;
import org.crysil.protocol.header.StandardHeader;
import org.crysil.protocol.payload.auth.AuthInfo;
import org.crysil.protocol.payload.auth.PayloadAuthRequest;
import org.crysil.protocol.payload.auth.PayloadAuthResponse;
import org.crysil.protocol.payload.auth.credentials.IdentifierAuthInfo;
import org.crysil.protocol.payload.auth.credentials.SecretAuthInfo;
import org.crysil.protocol.payload.auth.credentials.UserPasswordAuthInfo;
import org.crysil.protocol.payload.crypto.encrypt.PayloadEncryptRequest;
import org.crysil.protocol.payload.crypto.key.KeyHandle;
import org.crysil.protocol.payload.crypto.keydiscovery.PayloadDiscoverKeysRequest;
import org.crysil.protocol.payload.crypto.keydiscovery.PayloadDiscoverKeysResponse;
import org.crysil.protocol.payload.status.PayloadStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestTest {

	private String sessionID;
	private Gatekeeper DUT;
	private Connection connection;

	class TestActor implements Module {

		@Override
		public Response take(Request request) throws CrySILException {
			return new Response(request.getHeader(), new PayloadDiscoverKeysResponse());
		}
	}

	/**
	 * Create a fresh {@link crysilElement} and retrieve the {@link TestReceiver} before every test.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@BeforeMethod
	public void init() throws ClassNotFoundException, SQLException {
		DriverManager.getDriver("jdbc:mysql://localhost/cloudks_dev");
		connection = DriverManager.getConnection("jdbc:mysql://localhost/cloudks_dev",
				"cloudks",
				"cloudkspassword");
		
		DUT = new Gatekeeper(new GateKeeperConfiguration(connection));
		DUT.attach(new TestActor());
	}

	@Test(enabled = false)
	public void gettingStarted() throws AuthenticationFailedException, ClassNotFoundException, SQLException {
		GateKeeperConfiguration DUT = new GateKeeperConfiguration(connection);
		DUT.getAuthorizationProcess(null);
	}

	@Test
	public void testDiscoverKeysRequest() throws CrySILException {

		Request fixture = new Request(new StandardHeader(), new PayloadDiscoverKeysRequest());
		Response response0 = DUT.take(fixture);
		Assert.assertTrue(response0.getPayload() instanceof PayloadAuthResponse);
		Response response1 = DUT.take(answerIdentifierChallenge(response0, "identifier"));
		Assert.assertFalse(response1.getPayload() instanceof PayloadStatus);
	}

	@Test
	public void testEncryptRequestWrongAuth() throws CrySILException {
		Request fixture = createFixture("admin", "a");

		Response response0 = DUT.take(fixture);
		Assert.assertTrue(response0.getPayload() instanceof PayloadAuthResponse);
		Response response1 = DUT.take(answerSecretChallenge(response0, "wrong"));
		Assert.assertTrue(response1.getPayload() instanceof PayloadStatus);
	}

	@Test
	public void testEncryptRequestCorrectAuth() throws CrySILException {
		Request fixture = createFixture("admin", "a");

		Response response0 = DUT.take(fixture);
		Assert.assertTrue(response0.getPayload() instanceof PayloadAuthResponse);
		Response response1 = DUT.take(answerSecretChallenge(response0, "correct"));
		Assert.assertTrue(response1.getPayload() instanceof PayloadDiscoverKeysResponse);
	}

	@Test
	public void testEncryptRequestCorrect2StepAuth() throws CrySILException {
		Request fixture = createFixture("admin", "b");

		Response response0 = DUT.take(fixture);
		Assert.assertTrue(response0.getPayload() instanceof PayloadAuthResponse);
		Response response1 = DUT.take(answerSecretChallenge(response0, "correct"));
		Assert.assertTrue(response1.getPayload() instanceof PayloadAuthResponse);
		Response response2 = DUT.take(answerUsernamePasswordChallenge(response1, "username", "correct"));
		Assert.assertTrue(response2.getPayload() instanceof PayloadDiscoverKeysResponse);
	}

	@Test
	public void testEncryptRequestNonexistingKey() throws CrySILException {
		Request fixture = createFixture("nonexisting", "key");

		Response response0 = DUT.take(fixture);
		Assert.assertTrue(response0.getPayload() instanceof PayloadStatus);
	}

	private Request createFixture(String id, String subid) {
		PayloadEncryptRequest payload = new PayloadEncryptRequest();
		KeyHandle key = new KeyHandle();
		key.setId(id);
		key.setSubId(subid);
		payload.addEncryptionKey(key);
		payload.addPlainData("data".getBytes());
		Request fixture = new Request(new StandardHeader(), payload);
		return fixture;
	}

	protected Request createBasicRequest() {
		Request request = new Request();

		SessionHeader header = new StandardHeader();
		header.setSessionId(sessionID);
		request.setHeader(header);

		return request;
	}

	public Request answerIdentifierChallenge(Response challenge, String identifier) {
		IdentifierAuthInfo authInfo = new IdentifierAuthInfo();
		authInfo.setIdentifier(identifier);
		return createBasicAnswer(challenge, authInfo);
	}

	public Request answerSecretChallenge(Response challenge, String secret) {
		SecretAuthInfo authInfo = new SecretAuthInfo();
		authInfo.setSecret(secret);
		return createBasicAnswer(challenge, authInfo);
	}

	public Request answerUsernamePasswordChallenge(Response challenge, String username, String password) {
		UserPasswordAuthInfo authInfo = new UserPasswordAuthInfo();
		authInfo.setUserName(username);
		authInfo.setPassWord(password);
		return createBasicAnswer(challenge, authInfo);
	}

	public Request createBasicAnswer(Response challenge, AuthInfo authInfo) {
		Request authRequest = createBasicRequest();
		PayloadAuthRequest authRequestPayload = new PayloadAuthRequest();
		authRequestPayload.setAuthInfo(authInfo);
		authRequest.setPayload(authRequestPayload);
		authRequest.getHeader().setCommandId(challenge.getHeader().getCommandId());

		return authRequest;
	}
}
