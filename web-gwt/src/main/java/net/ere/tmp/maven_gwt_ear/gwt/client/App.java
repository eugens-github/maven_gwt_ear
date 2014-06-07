package net.ere.tmp.maven_gwt_ear.gwt.client;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class App implements EntryPoint {

	private Logger log = Logger.getLogger(App.class.getName());

	@Override
	public void onModuleLoad() {
		initForm();

		final Button button = new Button("Button");
		RootPanel.get("content").add(button);

		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				log.info("Clicked ...");
				button.setText(new Date().toString());
				callService();
			}

		});
	}

	private void initForm() {
		// Create a FormPanel and point it at a service.
		final FormPanel form = new FormPanel();
		// TODO dynamisieren
		form.setAction("http://localhost:8080/maven_gwt_ear-web/rest/users");

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		// form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// FormElement.as(form.getElement()).setAcceptCharset("UTF-8");

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// Create a TextBox, giving it a name so that it will be submitted.

		panel.add(new Label("Name:"));

		final TextBox tbName = new TextBox();
		tbName.setName("name");
		tbName.setText("Eügen");
		panel.add(tbName);

		panel.add(new Label("Email:"));
		TextBox btEmail = new TextBox();
		btEmail.setName("email");
		btEmail.setText("test@web.de");
		panel.add(btEmail);

		panel.add(new Label("Phone:"));
		TextBox tbPhone = new TextBox();
		tbPhone.setName("phoneNumber");
		tbPhone.setText("12345678901");
		panel.add(tbPhone);

		// Add a 'submit' button.
		panel.add(new Button("Register", new ClickHandler() {
			public void onClick(ClickEvent event) {
				// auf der Clientseite muss  der String encoded werden 
				String name = tbName.getText();
				String name2 = com.google.gwt.http.client.URL.encode(name);

				log.info("~~~~> name: '" + name + "'; name2='" + name2 + "'");

				tbName.setText(name2);

				form.submit();
				
				tbName.setText(name);
			}
		}));

		// Add an event handler to the form.
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {

				// This event is fired just before the form is submitted. We can take
				// this opportunity to perform validation.
				// if (tb.getText().length() == 0) {
				// Window.alert("The text box must not be empty");
				// event.cancel();
				// }
			}
		});
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// When the form submission is successfully completed, this event is
				// fired. Assuming the service returned a response of type text/html,
				// we can get the result text here (see the FormPanel documentation for
				// further explanation).
				Window.alert(event.getResults());
			}
		});

		RootPanel.get("content").add(form);
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
