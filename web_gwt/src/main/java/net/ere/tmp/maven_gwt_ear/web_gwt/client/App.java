package net.ere.tmp.maven_gwt_ear.web_gwt.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.widget.client.TextButton;

public class App implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final TextButton button = new TextButton("TextButton");

		RootPanel.get().add(button);

		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				button.setText(new Date().toString());

			}
		});
	}
}
