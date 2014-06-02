package net.ere.tmp.maven_gwt_ear.gwt.client;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class App implements EntryPoint {

	private Logger log = Logger.getLogger(App.class.getName());

	@Override
	public void onModuleLoad() {
		final Button button = new Button("Button");
		RootPanel.get().add(button);

		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				log.info("Clicked ...");
				button.setText(new Date().toString());
				callService();
			}

		});
	}

	private void callService() {
		// final String JSON_URL = GWT.getModuleBaseURL();
		final String JSON_URL = "http://localhost:8080/maven_gwt_ear-web/rest/members";
		String url = URL.encode(JSON_URL);

		log.fine("\n  URL=" + url + "\n  ModuleBaseURL=" + GWT.getModuleBaseURL());

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {
			@SuppressWarnings("unused")
			Request request = builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					log.severe("Couldn't retrieve JSON: exception=" + exception);
				}

				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						log.fine(response.getText());

						JsArray<MemberData> data = JsonUtils.safeEval(response.getText());

						StringBuilder sb = new StringBuilder("\n - - - Members - - -");

						data.get(1).setName("!! Geänderter Name");
						
						for (int i = 0; i < data.length(); i++) {
							MemberData d = data.get(i);
							sb.append("\ndata[" + i + "]: " + d.toMyString());
						}

						log.info(sb.toString());
					} else {
						log.warning("Couldn't retrieve JSON (" + response.getStatusText() + ")");
					}
				}
			});
		} catch (RequestException e) {
			log.severe("Couldn't retrieve JSON: " + e);
		}
	}
}
