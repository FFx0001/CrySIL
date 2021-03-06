package org.crysil.authentication.authplugins;

import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.crysil.authentication.AuthException;
import org.crysil.authentication.AuthHandler;
import org.crysil.authentication.AuthHandlerFactory;
import org.crysil.authentication.ui.ActionPerformedCallback;
import org.crysil.authentication.ui.IAuthUI;
import org.crysil.protocol.Response;
import org.crysil.protocol.payload.auth.AuthInfo;
import org.crysil.protocol.payload.auth.AuthType;
import org.crysil.protocol.payload.auth.credentials.SecretAuthInfo;
import org.crysil.protocol.payload.auth.credentials.SecretAuthType;

public class AuthPSK<T extends IAuthUI<char[], Void>> implements AuthHandler {

  private final Class<T> dialogType;

  public static class Factory<T extends IAuthUI<char[], Void>>
      implements AuthHandlerFactory<char[], Void, T> {

    private final Class<T> dialogType;

    public Factory(final Class<T> dialogType) {
      this.dialogType = dialogType;
    }

    @Override
    public AuthHandler createInstance(final Response crysilResponse, final AuthType authType,
        final Class<T> dialogType) throws AuthException {
      if (!canTake(crysilResponse, authType)) {
        throw new AuthException("Invalid authType");
      }

      return new AuthPSK<>(dialogType);
    }

    @Override
    public boolean canTake(final Response crysilResponse, final AuthType authType) throws AuthException {
      return (authType instanceof SecretAuthType);
    }

    @Override
    public Class<T> getDialogType() {
      return dialogType;
    }
  }

  public AuthPSK(final Class<T> dialogType) {
    this.dialogType = dialogType;
  }

  @Override
  public AuthInfo authenticate() throws AuthException {
    final CountDownLatch sync = new CountDownLatch(1);
    final AtomicReference<String> psk = new AtomicReference<>();

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          final T authUi = dialogType.newInstance();
          authUi.setCallbackAuthenticate(new ActionPerformedCallback() {
            @Override
            public void actionPerformed() {
              psk.set(new String(authUi.getAuthValue()));
              authUi.dismiss();
              sync.countDown();
            }
          });
          authUi.present();
        } catch (final InstantiationException e) {
          sync.countDown();
          e.printStackTrace();
        } catch (final IllegalAccessException e) {
          sync.countDown();
          e.printStackTrace();
        }
      }
    });

    try {
      sync.await();
    } catch (final InterruptedException e) {
      throw new AuthException("Error waiting for secret dialog", e);
    }

    final SecretAuthInfo authInfo = new SecretAuthInfo();
    authInfo.setSecret(psk.get());

    return authInfo;
  }

  @Override
  public String getFriendlyName() {
    return "Pre-Shared Secret";
  }

  @Override
  public boolean authenticatesAuthomatically() {
    return false;
  }
}
