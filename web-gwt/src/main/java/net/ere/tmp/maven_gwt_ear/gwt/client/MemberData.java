package net.ere.tmp.maven_gwt_ear.gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public class MemberData extends JavaScriptObject {
	// Overlay types always have protected, zero argument constructors.
	protected MemberData() {
	}

	public final native void setName(String name) /*-{
		this.name = name;
	}-*/;

	public final native String getName() /*-{
		return this.name;
	}-*/;

	public final native String getPhoneNumber() /*-{
		return this.phoneNumber;
	}-*/;

	public final native int getId() /*-{
		return this.id;
	}-*/;

	public final native double getEmail() /*-{
		return this.email;
	}-*/;

	public final String toMyString() {
		return "[" + getId() + "] " + getName() + " - " + getPhoneNumber();
	}
}
