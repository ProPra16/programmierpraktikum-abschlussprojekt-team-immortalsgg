package testTddController;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hhu.imtgg.compiler.TDDCompiler;

public class TDDCompilerTest {
	
	@Test
	public void checkCompileTestTrue() {
		TDDCompiler.getTestClass("blubbTest", "public class blubbTest {\npublic static void main(String[] args) {\n System.out.println(1);\n}\n}");
		TDDCompiler.getSourceClass("blubb", "public class blubb {\npublic static void main(String[] args) {\n System.out.println(1);\n}\n}");
		boolean compile = true;
		
		assertEquals(compile, TDDCompiler.checkCompile());
	}
	
	@Test
	public void checkCompileTestFalse() {
		TDDCompiler.getTestClass("blubbTest", "public class blubbTest {\npublic static void main(String[] args) {\n System.out.println(1);\n}\n}");
		TDDCompiler.getSourceClass("blubb", "public class blubb {\npublic static void main(String[] args) {\n System.out.printlnFALSE(1);\n}\n}");
		boolean compile = false;
		
		assertEquals(compile, TDDCompiler.checkCompile());
	}
	
}
