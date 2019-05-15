package ovgu.pave.handler.modelHandler;

import java.io.OutputStream;
import java.io.PrintStream;

public class SuppressConsolOutput {

	private SuppressConsolOutput() {
	}

	private static final PrintStream _standardPrintStream = System.out;

	public static void turnON() {
		System.setOut(new PrintStream(new OutputStream() {
			public void write(int b) {
				// DO NOTHING
			}
		}));
	}
	
	public static void turnOFF() {
		System.setOut(_standardPrintStream);
	}
}
