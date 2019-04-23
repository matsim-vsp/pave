package ovgu.pave.experiments.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReadCSV {

	@SuppressWarnings("unchecked")
	public static <T> List<List<T>> read(String path, String splitElement, Class<T> elementsOfClass,
			boolean readFirstLine) {
		List<List<T>> items = new ArrayList<List<T>>();
		Scanner scanIn = null;
		String InputLine = "";
		try {
			scanIn = new Scanner(new BufferedReader(new FileReader(path)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!readFirstLine)
			scanIn.nextLine();
		
		while (scanIn.hasNextLine()) {
			InputLine = scanIn.nextLine();
			String[] inArray = InputLine.split(splitElement);
			List<T> item = new ArrayList<T>();

			for (int x = 0; x < inArray.length; x++) {
				if (elementsOfClass.equals(int.class))
					item.add((T) Integer.valueOf(inArray[x]));
				if (elementsOfClass.equals(double.class))
					item.add((T) Double.valueOf(inArray[x]));
				if (elementsOfClass.equals(long.class))
					item.add((T) Long.valueOf(inArray[x]));
				if (elementsOfClass.equals(String.class))
					item.add((T) inArray[x]);
			}
			items.add(item);
		}
		scanIn.close();
		return items;
	}
}
