package de.hhu.imtgg.objects;

public class TDDCode {

	private String code;
	
	public TDDCode(String writtencode) { // sourcecode gewrappt
		this.code = writtencode;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public void setCode(String newwrittencode) {
		this.code = newwrittencode;
	}
}
