package org.crysil.authentication.authplugins;

import org.crysil.authentication.AuthenticationPlugin;
import org.crysil.authentication.AuthenticationPluginException;
import org.crysil.authentication.AuthenticationPluginFactory;
import org.crysil.authentication.ui.IAuthUI;
import org.crysil.protocol.Request;
import org.crysil.protocol.Response;
import org.crysil.protocol.header.Header;
import org.crysil.protocol.header.StandardHeader;
import org.crysil.protocol.payload.auth.AuthType;
import org.crysil.protocol.payload.auth.PayloadAuthRequest;
import org.crysil.protocol.payload.auth.debugnoauth.DebugNoAuthInfo;
import org.crysil.protocol.payload.auth.debugnoauth.DebugNoAuthType;

public class AuthDebugNoAuth<T extends IAuthUI<Void, Void>> implements AuthenticationPlugin {
  private final Response crysilResponse;

  public static class Factory<T extends IAuthUI<Void, Void>>
      implements AuthenticationPluginFactory<Void, Void, T> {

    private final Class<T> dialogType;

    public Factory(final Class<T> dialogType) {
      this.dialogType = dialogType;
    }

    @Override
    public AuthenticationPlugin createInstance(final Response crysilResponse, final AuthType authType,
        final Class<T> dialogType) throws AuthenticationPluginException {
      if (!canTake(crysilResponse, authType)) {
        throw new AuthenticationPluginException("Invalid authType");
      }

      return new AuthDebugNoAuth<>(crysilResponse, authType, dialogType);
    }

    @Override
    public boolean canTake(final Response crysilResponse, final AuthType authType)
        throws AuthenticationPluginException {
      return (authType instanceof DebugNoAuthType);
    }

    @Override
    public Class<T> getDialogType() {
      return dialogType;
    }
  }

  public AuthDebugNoAuth(final Response crysilResponse, final AuthType authType, final Class<T> dialogType) {
    this.crysilResponse = crysilResponse;
  }

  @Override
  public Request authenticate() throws AuthenticationPluginException {

    final Request authRequest = new Request();

    final Header header = new StandardHeader();
    header.setCommandId(crysilResponse.getHeader().getCommandId());
    authRequest.setHeader(header);

    final DebugNoAuthInfo authInfo = new DebugNoAuthInfo();

    final PayloadAuthRequest authRequestPayload = new PayloadAuthRequest();
    authRequestPayload.setAuthInfo(authInfo);
    authRequest.setPayload(authRequestPayload);

    return authRequest;
  }

  @Override
  public String getFriendlyName() {
    return "Debug No-Auth";
  }

  @Override
  public boolean authenticatesAuthomatically() {
    return false;
  }
}
