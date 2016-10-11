package org.crysil.instance.jce_receiver_demo.wizard;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.prefs.Preferences;

import org.crysil.communications.http.HttpJsonTransmitter;
import org.crysil.instance.jce_receiver_demo.Main;
import org.crysil.instance.jce_receiver_demo.model.Data;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

public class FetchKeys extends Step {

	public FetchKeys(final Data data) {
		setTitle("Fetching keys from Crysil Server");

		final ProgressIndicator pin = new ProgressIndicator();
		final StackPane hb = new StackPane();
		hb.getChildren().add(pin);

		setContent(hb);

		Task<Integer> task = new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {
				try {
					// set url
					((HttpJsonTransmitter) data.getProvider().getAttachedModule())
							.setTargetURI(Preferences.userNodeForPackage(Main.class).get("last", ""));

					// - fetch keys
					KeyStore keystore = KeyStore.getInstance("Crysil", data.getProvider());
					keystore.load(null); // isn't nice at all

					ArrayList<String> keyList = Collections.list(keystore.aliases());
					if(keyList.isEmpty())
                        procedeTo(new Message("An unexpected situation occured",
                                "You do not have any keys available\n or the key server is not reachable"));
					else
						procedeTo(new ChooseKey(data, keystore, keyList));
				} catch (final Exception e) {
					e.printStackTrace();
					procedeTo(new Message("An error has occured.", e.getMessage()));
				}

				return null;
			}
		};

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
}
