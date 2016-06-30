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
	
	@Test
	public void checkTestsAllSuccessTrue() {
		TDDCompiler.setTestClass("blubbTest", "import static org.junit.Assert.*;\nimport org.junit.Test;\npublic class blubbTest {\n@Test\npublic void zahlTest1(){assertEquals(1,blubb.zahl());}\n@Test\npublic void zahlTest2(){assertEquals(1,blubb.zahl());}}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static int zahl() {\n return 1;\n}\n}");
		boolean compile = true;
		
		assertEquals(compile, TDDCompiler.checkTestsAllSuccess());
	}
	
	@Test
	public void checkTestsAllSuccessFalse() {
		TDDCompiler.setTestClass("blubbTest", "import static org.junit.Assert.*;\nimport org.junit.Test;\npublic class blubbTest {\n@Test\npublic void zahlTest1(){assertEquals(1,blubb.zahl());}\n@Test\npublic void zahlTest2(){assertEquals(2,blubb.zahl());}}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static int zahl() {\n return 1;\n}\n}");
		boolean compile = false;
		
		assertEquals(compile, TDDCompiler.checkTestsAllSuccess());
	}
	
	@Test
	public void getCompileErrorsTest1() {
		TDDCompiler.setTestClass("blubbTest", "public class blubbTest {\npublic static void main(String[] args) {\n System.out.printlnFALSE(1);\n}\n}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static void main(String[] args) {\n System.out.println(1);\n}\n}");
		
		assertEquals("[blubbTest.java:3:error:cannot find symbol\n"
		+ "  symbol:   method printlnFALSE(int)\n"
		+ "  location: variable out of type java.io.PrintStream\n"
		+ " System.out.printlnFALSE(1);\n"
		+ "           ^]",TDDCompiler.getCompileErrors(1));
	}
	
	@Test
	public void getCompileErrorsTest2() {
		TDDCompiler.setTestClass("blubbTest", "public class blubbTest {\npublic static void main(String[] args) {\n System.out.println(1);\n}\n}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static void main(String[] args) {\n System.out.printlnFALSE(1);\n}\n}");
		
		assertEquals("[blubb.java:3:error:cannot find symbol\n"
		+ "  symbol:   method printlnFALSE(int)\n"
		+ "  location: variable out of type java.io.PrintStream\n"
		+ " System.out.printlnFALSE(1);\n"
		+ "           ^]",TDDCompiler.getCompileErrors(2));
	}
	
	@Test
	public void getTestFailsTest() {
		TDDCompiler.setTestClass("blubbTest", "import static org.junit.Assert.*;\nimport org.junit.Test;\npublic class blubbTest {\n@Test\npublic void zahlTest1(){assertEquals(1,blubb.zahl());}\n@Test\npublic void zahlTest2(){assertEquals(2,blubb.zahl());}}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static int zahl() {\n return 1;\n}\n}");
		
		assertEquals("expected:<2> but was:<1>\n", TDDCompiler.getTestFails());
	}
	
	@Test
	public void ResetFailsAndgetTestFailsAllTestsPassTest() {
		TDDCompiler.setTestClass("blubbTest", "import static org.junit.Assert.*;\nimport org.junit.Test;\npublic class blubbTest {\n@Test\npublic void zahlTest(){assertEquals(1,blubb.zahl());}}");
		TDDCompiler.setSourceClass("blubb", "public class blubb {\npublic static int zahl() {\n return 1;\n}\n}");
		TDDCompiler.resetFails();
		
		assertEquals("", TDDCompiler.getTestFails());
	}
}
