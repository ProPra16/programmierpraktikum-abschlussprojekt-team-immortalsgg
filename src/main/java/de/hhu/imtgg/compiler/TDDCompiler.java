package de.hhu.imtgg.compiler;

import vk.core.api.CompilationUnit;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

public class TDDCompiler {
	
	private static CompilationUnit testClass;
	private static CompilationUnit sourceClass;
	
	public static void getTestClass(String testName,String testCode) {
		testClass = new CompilationUnit(testName,testCode,true);
	}
	public static void getSourceClass(String sourceName,String sourceCode) {
		sourceClass = new CompilationUnit(sourceName,sourceCode,false);
	}
	
	public static boolean checkCompile() { //  check ob es compiled // es muss eine loesung er damit es nicht 3fach gemacht wird
		JavaStringCompiler compiler = CompilerFactory.getCompiler(testClass,sourceClass);
		compiler.compileAndRunTests();
		
		return compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(sourceClass).isEmpty()
				&& compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(testClass).isEmpty();
	}
	public static boolean checkTests1Failed() { // checkt ob nur 1 test failt 
		JavaStringCompiler compiler = CompilerFactory.getCompiler(testClass,sourceClass);
		compiler.compileAndRunTests();
		
		return !(compiler.getTestResult().getNumberOfFailedTests() > 1);
	}
	
	public static boolean checkTestsAllSuccess() { // checkt ob alle tests failen
		JavaStringCompiler compiler = CompilerFactory.getCompiler(testClass,sourceClass);
		compiler.compileAndRunTests();
		
		return compiler.getTestResult().getNumberOfFailedTests() == 0;
	}
	
}
