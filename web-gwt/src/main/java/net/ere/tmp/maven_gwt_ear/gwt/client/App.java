package net.ere.tmp.maven_gwt_ear.gwt.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class App implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final Button button = new Button("Button");
		RootPanel.get().add(button);

		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				button.setText(new Date().toString());
			}
		});
	}
}
