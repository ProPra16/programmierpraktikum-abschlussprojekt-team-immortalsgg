package testTddController;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hhu.imtgg.compiler.TDDCompiler;

public class TDDCompilerTest {
	
	@Test
	public void checkCompileTestTrue() {
		TDDCompiler.setTestClass("blubbTest", "public class blubbTest {\npublic static void main(String[] args) {\n System.out.println(1);\n}\n}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static void main(String[] args) {\n System.out.println(1);\n}\n}");
		boolean compile = true;
		
		assertEquals(compile, TDDCompiler.checkCompile());
	}
	
	@Test
	public void checkCompileTestFalse() {
		TDDCompiler.setTestClass("blubbTest", "public class blubbTest {\npublic static void main(String[] args) {\n System.out.println(1);\n}\n}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static void main(String[] args) {\n System.out.printlnFALSE(1);\n}\n}");
		boolean compile = false;
		
		assertEquals(compile, TDDCompiler.checkCompile());
	}
	
	@Test
	public void checkTests1FailedTestTrue() {
		TDDCompiler.setTestClass("blubbTest", "import static org.junit.Assert.*;\nimport org.junit.Test;\npublic class blubbTest {\n@Test\npublic void zahlTest1(){assertEquals(1,blubb.zahl());}\n@Test\npublic void zahlTest2(){assertEquals(2,blubb.zahl());}}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static int zahl() {\n return 1;\n}\n}");
		boolean compile = true;
		
		assertEquals(compile, TDDCompiler.checkTests1Failed());
	}
	
	@Test
	public void checkTests1FailedTestNoTestFailed() {
		TDDCompiler.setTestClass("blubbTest", "import static org.junit.Assert.*;\nimport org.junit.Test;\npublic class blubbTest {\n@Test\npublic void zahlTest1(){assertEquals(1,blubb.zahl());}\n@Test\npublic void zahlTest2(){assertEquals(1,blubb.zahl());}}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static int zahl() {\n return 1;\n}\n}");
		boolean compile = false;
		
		assertEquals(compile, TDDCompiler.checkTests1Failed());
	}
	
	@Test
	public void checkTests1FailedTestTwoTestFailed() {
		TDDCompiler.setTestClass("blubbTest", "import static org.junit.Assert.*;\nimport org.junit.Test;\npublic class blubbTest {\n@Test\npublic void zahlTest1(){assertEquals(2,blubb.zahl());}\n@Test\npublic void zahlTest2(){assertEquals(2,blubb.zahl());}}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static int zahl() {\n return 1;\n}\n}");
		boolean compile = false;
		
		assertEquals(compile, TDDCompiler.checkTests1Failed());
	}
}
