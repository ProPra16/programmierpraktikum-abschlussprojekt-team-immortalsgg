
import org.junit.Test;

import vk.core.api.CompilationUnit;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

public class DefaultTest {

	@Test
	public void testSomething() {
		CompilationUnit sourceClass = new CompilationUnit("Bar" ,"public class Bar { \n"
				+ " public static int fourtyTwo() { \n"
				+ "    return 41 + 1; \n"
				+ " }\n"
				+ "}" ,false);
		CompilationUnit otherClass = new CompilationUnit("Foo","public class Foo {}",true);
		JavaStringCompiler compiler = CompilerFactory.getCompiler(sourceClass);
		compiler.compileAndRunTests();
	}
		
}
