package ovgu.pave.handler.modelHandler;

import org.eclipse.emf.ecore.util.EcoreUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ovgu.pave.handler.modelHandler.IOHandler;
import ovgu.pave.model.config.Config;
import ovgu.pave.model.input.Input;
import ovgu.pave.model.solution.Solution;

public class IOTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public IOTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(IOTests.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	
	protected static Config loadConfig() {
		return IOHandler.loadConfig("test/testConfig");
	}
	
	protected static Input loadInput() {
		return IOHandler.loadInput("test/", "testInput");
	}
	
	protected static Solution loadSolution(Input input) {
		return IOHandler.loadSolution("test/", "testSolution", input);
	}

	public void test_loadInsane() {
		// config, input, and solution must have been saved before
		loadInput();
		loadConfig();
		loadInput();
		loadSolution(loadInput());
		loadConfig();
		loadSolution(loadInput());
		loadInput();
		loadConfig();
		loadSolution(loadInput());
		loadInput();
		loadConfig();
	}

	public void test_IO_config_input_solution() {
		config_IO_Test();
		input_IO_Test();
		solution_IO_Test(false);
		solution_IO_Test(true);
	}

	private void config_IO_Test() {

		/*
		 * generate config1
		 */

		Config config1 = GenerateTestData.generateTestConfig();

		/*
		 * save config1
		 */

		String path = "test/testConfig";
		IOHandler.saveConfig(config1, path);

		/*
		 * read into config2
		 */

		Config config2 = IOHandler.loadConfig(path);

		/*
		 * test config1 equals config2
		 */

		assertTrue(EcoreUtil.equals(config1, config2));
	}

	private void input_IO_Test() {

		/*
		 * generate input1
		 */

		Input input1 = GenerateTestData.generateTestInput();

		/*
		 * save input1
		 */

		String configInputFolder = "test/";
		String filename = "testInput";
		IOHandler.saveInput(input1, configInputFolder, filename);

		/*
		 * read into input2
		 */

		Input input2 = IOHandler.loadInput(configInputFolder, filename);

		/*
		 * test input1 equals input2
		 */

		assertTrue(EcoreUtil.equals(input1, input2));
	}

	private void solution_IO_Test(boolean useVehicleType) {

		/*
		 * read input
		 */

		Input input = IOHandler.loadInput("test/", "testInput");

		/*
		 * generate solution1
		 */

		Solution solution1 = GenerateTestData.generateTestSolution(input, useVehicleType);

		/*
		 * save solution1
		 */

		String configOutputFolder = "test/";
		String filename = "testSolution";
		IOHandler.saveSolution(solution1, configOutputFolder, filename);

		/*
		 * read into solution2
		 */

		Solution solution2 = IOHandler.loadSolution(configOutputFolder, filename, input);

		/*
		 * test variables/solution1 equals solution2
		 */

		assertTrue(EcoreUtil.equals(solution1, solution2));
	}
}
