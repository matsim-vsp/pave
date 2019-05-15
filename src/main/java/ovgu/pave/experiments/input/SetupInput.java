package ovgu.pave.experiments.input;

import ovgu.pave.handler.Handler;

public class SetupInput {
	private static final String _DefaultConfigPath = "../config.xml";

	public static void main(String[] args) {
		System.out.println("run SetupInput in experiments");
		Handler.getInput().loadConfig(_DefaultConfigPath);
		
		
//		Handler.getInput().getConfig().setInputFilename("inputHDP.zip");
//		ReadJarmoSzenarioHDP.readData(folderPath()+"HDP/");
		
		Handler.getInput().getConfig().setInputFilename("inputDARP.zip");
		ReadJarmoEWGT.readData(folderPath()+"DARP/");
	
		System.out.println("save input ...");
		Handler.getInput().saveInput(Handler.getInput().getConfig().getInputFilename());
		System.out.println("SetupInput in experiments is finished");
	}

	private static String folderPath() {
		return System.getProperty("user.dir") + "/" + Handler.getInput().getConfig().getInputFolder() + "/";
	}
}
