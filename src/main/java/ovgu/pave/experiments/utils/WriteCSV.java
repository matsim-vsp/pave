package ovgu.pave.experiments.utils;

import java.util.Date;
import java.util.List;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import ovgu.pave.handler.Handler;

public class WriteCSV {
	
	public static void write(List<List<String>> values){

		FileWriter writer;
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Handler.getInput().getConfig().getExperiment().getOutputFolder());
		stringBuilder.append("/");
		stringBuilder.append(dateFormat.format(date));
		stringBuilder.append(".csv");
		
		try {
			writer = new FileWriter(stringBuilder.toString());
			for (int i = 0; i < values.size(); i++) {
				for (int j = 0; j < values.get(i).size() ; j++) {
					writer.append(String.valueOf(values.get(i).get(j)));
					writer.append(',');
				}
				writer.append('\n');
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
