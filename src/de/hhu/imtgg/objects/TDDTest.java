package de.hhu.imtgg.objects;

public class TDDTest {
	
	private String testcode;
	
	public TDDTest(String testcode) {  //testcode gewrappt
		this.testcode = testcode;
	}
	
	public String getTestCode() {
		return testcode;
	}
	
	public void setTestCode(String newtestcode) {
		testcode = newtestcode;
	}
	
}
