package net.ere.tmp.maven_gwt_ear.gwt.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class App implements EntryPoint {

    private Logger log = Logger.getLogger(App.class.getName());

    private final FormPanel newMembarForm = new FormPanel();
    private CellTable<MemberData> table = new CellTable<MemberData>();
    private final TextBox tbName = new TextBox();
    private final TextBox tvEmail = new TextBox();
    private final TextBox tbPhone = new TextBox();

    final Button btnRegister = new Button("Register");
    final Button btnSubmit = new Button("Send data over FormPanel.submit()");

    @Override
    public void onModuleLoad() {
        initNewMemberForm();

        // RootPanel.get("content").add(btnSubmit);

        initMemberTable();
        loadMember();

        btnSubmit.addClickHandler(new SubmitClickHandler());

        btnRegister.addClickHandler(new SendJsonClickHandler());
    }

    private void initMemberTable() {
        table.setStylePrimaryName("simpletablestyle");

        TextColumn<MemberData> idColumn = new TextColumn<MemberData>() {
            @Override
            public String getValue(MemberData member) {
                return "" + member.getId();
            }
        };

        TextColumn<MemberData> nameColumn = new TextColumn<MemberData>() {
            @Override
            public String getValue(MemberData member) {
                return member.getName();
            }
        };

        TextColumn<MemberData> emailColumn = new TextColumn<MemberData>() {
            @Override
            public String getValue(MemberData member) {
                return member.getEmail();
            }
        };

        TextColumn<MemberData> phoneColumn = new TextColumn<MemberData>() {
            @Override
            public String getValue(MemberData member) {
                return member.getPhoneNumber();
            }
        };


        Column<MemberData, String> urlColumn = new Column<MemberData, String>(new MyClickableCellText()) {
            @Override
            public String getValue(MemberData object) {
                return "/rest/members/" + object.getId();
            }
        };

        // table.addColumn(idColumn, "Id");

        SafeHtmlBuilder headerBuilder = new SafeHtmlBuilder();
        headerBuilder.appendEscaped("Id");
        SafeHtmlBuilder footerBuilder = new SafeHtmlBuilder();
        footerBuilder.appendEscaped("REST URL for all members:");

        table.addColumn(idColumn, headerBuilder.toSafeHtml(), footerBuilder.toSafeHtml());

        // TODO add colspan to footer

        table.addColumn(nameColumn, "Name");
        table.addColumn(emailColumn, "Email");
        table.addColumn(phoneColumn, "Phone #");
        table.addColumn(urlColumn, "REST URL");

        HeadingElement headingElement = Document.get().createHElement(2);
        headingElement.setInnerText("Members");
        RootPanel.get("content").getElement().appendChild(headingElement);

        RootPanel.get("content").add(table);
    }

    private void initNewMemberForm() {
        String url = getFullUrl("rest/users");
        newMembarForm.setAction(url);
        newMembarForm.setMethod(FormPanel.METHOD_POST);

        // final VerticalPanel panel = new VerticalPanel();
        final FlowPanel panel = new FlowPanel();
        panel.addStyleName("new_member_form");

        HeadingElement headingElement = Document.get().createHElement(2);
        headingElement.setInnerText("Member Registration");
        newMembarForm.getElement().appendChild(headingElement);

        ParagraphElement pElement = Document.get().createPElement();
        pElement.setInnerText("Enforces annotation-based constraints defined on the model class.");
        newMembarForm.getElement().appendChild(pElement);

        newMembarForm.setWidget(panel);

        panel.add(new Label("Name:"));
        tbName.setName("name");
        tbName.setText("Eügen");
        panel.add(tbName);

        panel.add(new Label("Email:"));

        tvEmail.setName("email");
        tvEmail.setText("test@web.de");
        panel.add(tvEmail);

        panel.add(new Label("Phone:"));

        tbPhone.setName("phoneNumber");
        tbPhone.setText("12345678901");
        panel.add(tbPhone);

        panel.add(btnRegister);

        // Add an event handler to the form.
        newMembarForm.addSubmitHandler(new FormPanel.SubmitHandler() {
            public void onSubmit(SubmitEvent event) {

                // This event is fired just before the form is submitted. We can take
                // this opportunity to perform validation.
                // if (tb.getText().length() == 0) {
                // Window.alert("The text box must not be empty");
                // event.cancel();
                // }
            }
        });
        newMembarForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            public void onSubmitComplete(SubmitCompleteEvent event) {
                // When the form submission is successfully completed, this event is
                // fired. Assuming the service returned a response of type text/html,
                // we can get the result text here (see the FormPanel documentation for
                // further explanation).
                Window.alert(event.getResults());
            }
        });

        RootPanel.get("content").add(newMembarForm);
    }

    private void loadMember() {
        String url = getFullUrl("rest/members");

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

                        List<MemberData> member = new ArrayList<>();

                        for (int i = 0; i < data.length(); i++) {
                            MemberData d = data.get(i);
                            sb.append("\ndata[" + i + "]: " + d.toMyString());
                            member.add(d);
                        }

                        table.setRowCount(member.size(), true);
                        table.setRowData(0, member);

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

    private void sendNewMemberData(final MemberData newMember) {
        String url = getFullUrl("rest/members");

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
        builder.setHeader("Content-Type", "application/json;charset=utf-8");

        String requestData = new JSONObject(newMember).toString();

        try {
            @SuppressWarnings("unused")
            Request request = builder.sendRequest(requestData, new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                    log.severe("Couldn't retrieve JSON: exception=" + exception);
                }

                public void onResponseReceived(Request request, Response response) {
                    if (200 == response.getStatusCode()) {
                        log.fine("Result on sendNewMemberData() = " + response.getText());
                    } else {
                        log.warning("Couldn't retrieve JSON (" + response.getStatusText() + ")");
                    }
                }
            });
        } catch (RequestException e) {
            log.severe("Couldn't retrieve JSON: " + e);
        }
    }

    private String getFullUrl(String part) {
        @SuppressWarnings("unused")
        String hostPageBaseURL = GWT.getHostPageBaseURL(); // http://localhost:8080/maven_gwt_ear-web-gwt/

        if (part.startsWith("/"))
            return URL.encode("http://localhost:8080/maven_gwt_ear-web" + part);
        else
            return URL.encode("http://localhost:8080/maven_gwt_ear-web/" + part);
    }

    private class SubmitClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            String name = tbName.getText();
            String name2 = com.google.gwt.http.client.URL.encode(name);

            log.info("~~~~> name: '" + name + "'; name2='" + name2 + "'");

            tbName.setText(name2);

            newMembarForm.submit();

            tbName.setText(name);
        }
    }

    private class SendJsonClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            MemberData newMember = (MemberData) JavaScriptObject.createObject().cast();
            newMember.setName(tbName.getText());
            newMember.setEmail(tvEmail.getText());
            newMember.setPhoneNumber(tbPhone.getText());
            sendNewMemberData(newMember);
        }
    }

    public class MyClickableCellText extends ClickableTextCell {

        public MyClickableCellText() {
        }

        @Override
        protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
            if (value != null) {
                sb.appendHtmlConstant("<a href=\""+ getFullUrl(value.asString())+"\">");
                sb.append(value);
                sb.appendHtmlConstant("</a>");
            }
        }
    }
}
