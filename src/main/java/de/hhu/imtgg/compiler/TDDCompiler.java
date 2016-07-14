package de.hhu.imtgg.compiler;

import java.util.ArrayList;
import java.util.List;

import vk.core.api.CompilationUnit;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

public class TDDCompiler {
	
	private static CompilationUnit testClass;
	private static CompilationUnit sourceClass;
	
	private static String fails = "";
	
	
	/**
	 * weist {@link testClass} aktuelle werte zu
	 * den namen der klasse und den code der drin steht
	 * @param testName
	 * @param testCode
	 */
	public static void setTestClass(String testName,String testCode) {
		testClass = new CompilationUnit(testName,testCode,true);
	}
	
	/**
	 * weist {@link sourceClass} aktuelle werte zu
	 * den namen der klasse und den code der drin steht
	 * @param sourceName
	 * @param sourceCode
	 */
	public static void setSourceClass(String sourceName,String sourceCode) {
		sourceClass = new CompilationUnit(sourceName,sourceCode,false);
	}
	
	/**
	 * prüft ob {@link testClass} und {@link sourceClass} compilierbar sind
	 * @return true / false
	 */
	public static boolean checkCompile() { //  check ob es compiled // es muss eine loesung er damit es nicht 3fach gemacht wird
		JavaStringCompiler compiler = CompilerFactory.getCompiler(testClass,sourceClass);
		compiler.compileAndRunTests();
		
		return compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(sourceClass).isEmpty()
				&& compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(testClass).isEmpty();
	}
	
	/**
	 * prüft ob {@link testClass} nur einen fehlschlagenden test beinhaltet
	 * @return true / false
	 */
	public static boolean checkTests1Failed() { // checkt ob nur 1 test failt 
		JavaStringCompiler compiler = CompilerFactory.getCompiler(testClass,sourceClass);
		compiler.compileAndRunTests();
		
		return compiler.getTestResult().getNumberOfFailedTests() == 1;
	}
	
	/**
	 * prüft ob {@link testClass} alle tests bestanden werden
	 * @return true / false
	 */
	public static boolean checkTestsAllSuccess() { // checkt ob alle tests failen
		JavaStringCompiler compiler = CompilerFactory.getCompiler(testClass,sourceClass);
		compiler.compileAndRunTests();
		
		return compiler.getTestResult().getNumberOfFailedTests() == 0;
	}
	
	/**
	 * sammelt compiler errors in einem String 
	 * gibt entweder für die {@link sourceClass} oder für die {@link testClass} die  compiler errors zurück
	 * @param klasse 1 oder 2 entscheidet ob man die errors von der testclass oder von der sourceclass haben möchte
	 * @return Compile Errors as String of sourceclass or testclass
	 */
	public static String getCompileErrors(int klasse) {
		JavaStringCompiler compiler = CompilerFactory.getCompiler(testClass,sourceClass);
		compiler.compileAndRunTests();
		
		if(klasse == 1) return compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(testClass).toString();
		if(klasse == 2) return compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(sourceClass).toString();
		else return "";
	}
	
	/**
	 * sammelt alle fehlschlagende tests in einem String 
	 * @return einen String welcher alle fehlschlagende testresultate beeinhaltet
	 */
	public static String getTestFails() { // gibt alle falschen tests an
		JavaStringCompiler compiler = CompilerFactory.getCompiler(testClass,sourceClass);
		compiler.compileAndRunTests();
		
		compiler.getTestResult().getTestFailures().stream().forEach(e -> { fails = fails + e.getMessage() +"\n";});
		return fails;
	}
	
	/**
	 * resetet {@link fails} damit für den nächsten durchgang alles sauber ist
	 */
	public static void resetFails() {
		fails = "";
	}
}
