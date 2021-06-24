package de.sranko_informatik.si_xml_to_pdf_jar_gui;

public class JSONPayload {
	private String template;
	private String data;
	private String datenTyp;
	
	public String getTemplate() {
		return this.template;
	}
	
	public String getData() {
		return this.data;
	}
	
	public String getDatenTyp() {
		return this.datenTyp;
	}
	
	public String toString() {
		return this.template + " | " + this.datenTyp + " | " + this.data; 
	}
}