package ovgu.pave.handler;

import junit.textui.TestRunner;
import ovgu.pave.handler.modelHandler.IOTests;
import ovgu.pave.handler.modelHandler.InputHandlerTests;
import ovgu.pave.handler.modelHandler.NetworkHandlerTests;
import ovgu.pave.handler.modelHandler.SolutionHandlerTests;
import ovgu.pave.model.input.InputFactory;
import ovgu.pave.model.network.NetworkFactory;
import ovgu.pave.model.solution.SolutionFactory;

public class RunTests {

	public static void main(String[] args) {

		InputFactory.eINSTANCE.getClass();
		NetworkFactory.eINSTANCE.getClass();
		SolutionFactory.eINSTANCE.getClass();
		
		
		runTest(IOTests.class);
		runTest(InputHandlerTests.class);
		runTest(NetworkHandlerTests.class);
		runTest(SolutionHandlerTests.class);

	}

	private static void runTest(Class<?> clas) {
		System.out.println("run " + clas.getSimpleName());
		TestRunner.run(clas);
	}

}
